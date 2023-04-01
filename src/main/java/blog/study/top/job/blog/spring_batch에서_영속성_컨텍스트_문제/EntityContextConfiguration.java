package blog.study.top.job.blog.spring_batch에서_영속성_컨텍스트_문제;

import blog.study.top.repository.product.History;
import blog.study.top.repository.product.PurchaseOrder;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class EntityContextConfiguration {

	private final EntityManagerFactory entityManagerFactory;
	private final PlatformTransactionManager transactionManager;
	private final JobRepository jobRepository;
	private static final int chunkSize = 100;

	@Bean
	public Job job230401_2() {
		return new JobBuilder("230401Job_2", jobRepository)
				.start(step230401_2())
				.build();
	}

	@Bean
	public Step step230401_2() {
		return new StepBuilder("step230401_2", jobRepository)
				.<PurchaseOrder, History>chunk(chunkSize, transactionManager)
				.reader(reader230401_2())
				.processor(processor230401_2())
				.writer(writer230401_2())
				.build();
	}

	@Bean
	public JpaPagingItemReader<PurchaseOrder> reader230401_2() {
		return new JpaPagingItemReaderBuilder<PurchaseOrder>()
				.name("reader230401_2")
				.pageSize(chunkSize)
				.queryString("select o from PurchaseOrder o")
				.entityManagerFactory(entityManagerFactory)
				.build();
	}


	public ItemProcessor<PurchaseOrder, History> processor230401_2() {
		return item -> History.builder()
				.purchaseOrderId(item.getId())
				.productIdList(item.getProductList())
				.build();
	}

	@Bean
	public JpaItemWriter<History> writer230401_2() {
		return new JpaItemWriterBuilder<History>()
				.entityManagerFactory(entityManagerFactory)
				.build();
	}
}
