package blog.study.top.job.blog.spring_batch_itemReader에서_readerDB_사용하기;

import blog.study.top.repository.product.Product;
import blog.study.top.repository.product.ProductBackup;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ProductBackupConfiguration {
	private final JobRepository jobRepository;
	private final EntityManagerFactory entityManagerFactory;
//	@Qualifier("readerEntityManagerFactory")
//	private final EntityManagerFactory readerEntityManagerFactory;
	private final PlatformTransactionManager transactionManager;
	private final int chunkSize = 10;

	@Bean
	public Job job_230408() {
		return new JobBuilder("job_230408", jobRepository)
				.start(step_230408())
				.build();
	}

	@Bean
	public Step step_230408() {
		return new StepBuilder("step_230408", jobRepository)
				.<Product, ProductBackup>chunk(chunkSize, transactionManager)
				.reader(reader_230408(null))
				.processor(processor())
				.writer(writer_230408())
				.build();
	}
	@Bean
	@StepScope
	public JpaPagingItemReader<Product> reader_230408(
			@Value("#{jobParameters[txDate]}") String txDate
	) {
		String query = String.format("SELECT p FROM Product p WHERE p.createDate = '%s'", txDate);

		return new JpaPagingItemReaderBuilder<Product>()
				.entityManagerFactory(entityManagerFactory)
				.queryString(query)
				.pageSize(chunkSize)
				.name("reader_230408")
				.build();
	}

	private ItemProcessor<Product, ProductBackup> processor() {
		return ProductBackup::new;
	}

	@Bean
	public JpaItemWriter<ProductBackup> writer_230408() {
		return new JpaItemWriterBuilder<ProductBackup>()
				.entityManagerFactory(entityManagerFactory)
				.build();
	}
}
