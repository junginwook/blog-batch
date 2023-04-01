package blog.study.top.job.blog.itemWriter에_list_전달하기;

import blog.study.top.repository.tax.Sales;
import blog.study.top.repository.tax.Tax;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ItemListJobConfiguration {
	private final JobRepository jobRepository;
	private static final int chunkSize = 10;
	private final PlatformTransactionManager transactionManager;
	private final EntityManagerFactory entityManagerFactory;

	@Bean
	public Job itemListJob() {
		return new JobBuilder("itemListJob", jobRepository)
				.start(itemListStep())
				.build();
	}

	@Bean
	public Step itemListStep() {
		return new StepBuilder("itemListStep", jobRepository)
				.<Sales, List<Tax>>chunk(chunkSize, transactionManager)
				.reader(itemListReader())
				.processor(itemListProcessor())
				.writer(itemListWriter())
				.build();
	}

	@Bean
	@StepScope
	public JpaPagingItemReader<Sales> itemListReader() {
		return new JpaPagingItemReaderBuilder<Sales>()
				.name("itemListReader")
				.queryString("select s from Sales s")
				.entityManagerFactory(entityManagerFactory)
				.build();
	}

	@Bean
	public ItemProcessor<Sales, List<Tax>> itemListProcessor() {
		return (sales) -> List.of(
				new Tax(sales.getTxAmount(), sales.getOwnerNo()),
				new Tax((long)(sales.getTxAmount()/1.1), sales.getOwnerNo()),
				new Tax(sales.getTxAmount()/11, sales.getOwnerNo())
		);
	}

	@Bean
	public ListJpaPagingItemWriter<Tax> itemListWriter() {
		JpaItemWriter<Tax> writer = new JpaItemWriterBuilder<Tax>()
				.entityManagerFactory(entityManagerFactory)
				.build();

		return new ListJpaPagingItemWriter<>(writer, entityManagerFactory);
	}
}
