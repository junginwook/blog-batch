package blog.study.top.job.blog.itemWriter;

import blog.study.top.repository.pay.PayEntity;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaItemWriterJobConfiguration {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final EntityManagerFactory entityManagerFactory;
	private int CHUNK_SIZE = 10;

	@Bean
	public Job jpaItemWriterJob() {
		return new JobBuilder("jpaItemWriterJob", jobRepository)
				.start(jpaItemWriterStep())
				.build();
	}

	@Bean
	public Step jpaItemWriterStep() {
		return new StepBuilder("jpaItemWriterStep", jobRepository)
				.<PayEntity, PayEntity>chunk(CHUNK_SIZE, transactionManager)
				.reader(jpaItemWriterReader())
				.processor(jpaItemProcessor())
				.writer(jpaItemWriter())
				.build();
	}

	@Bean
	public JpaPagingItemReader<PayEntity> jpaItemWriterReader() {
		return new JpaPagingItemReaderBuilder<PayEntity>()
				.name("jpaItemWriterReader")
				.entityManagerFactory(entityManagerFactory)
				.pageSize(CHUNK_SIZE)
				.queryString("SELECT p FROM pay p")
				.build();
	}

	@Bean
	public ItemProcessor<PayEntity, PayEntity> jpaItemProcessor() {
		return pay -> pay;
	}

	@Bean
	public JpaItemWriter<PayEntity> jpaItemWriter() {
		JpaItemWriter<PayEntity> jpaItemWriter = new JpaItemWriter<>();
		jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
		jpaItemWriter.setUsePersist(true);
		return jpaItemWriter;
	}
}
