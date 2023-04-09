package blog.study.top.job.blog.spring_batch_jpaCursorItemReader도입;

import blog.study.top.repository.pay.PayEntity;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaCursorItemReaderJobConfig {
	private final JobRepository jobRepository;
	private final EntityManagerFactory entityManagerFactory;
	private final PlatformTransactionManager transactionManager;

	private static final int chunkSize = 10;

	@Bean
	public Job job_230409() {
		return new JobBuilder("job_230409", jobRepository)
				.start(step_230409())
				.build();
	}

	@Bean
	public Step step_230409() {
		return new StepBuilder("step_230409", jobRepository)
				.<PayEntity, PayEntity>chunk(chunkSize, transactionManager)
				.reader(reader_230409())
				.writer(jpaCursorItemWriter())
				.build();
	}

	@Bean
	public JpaCursorItemReader<PayEntity> reader_230409() {
		return new JpaCursorItemReaderBuilder<PayEntity>()
				.name("reader_230409")
				.entityManagerFactory(entityManagerFactory)
				.queryString("SELECT p FROM pay p")
				.build();
	}

	private ItemWriter<PayEntity> jpaCursorItemWriter() {
		return list -> {
			for(PayEntity payEntity: list) {
				log.info("Current Pay={}", payEntity);
			}
		};
	}
}
