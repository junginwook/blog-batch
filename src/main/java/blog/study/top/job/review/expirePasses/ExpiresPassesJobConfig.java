package blog.study.top.job.review.expirePasses;

import blog.study.top.repository.pass.PassEntity;
import blog.study.top.repository.pass.PassStatus;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class ExpiresPassesJobConfig {

	private static final int CHUNK_SIZE = 10;

	private final EntityManagerFactory entityManagerFactory;

	@Bean
	public Job expiresPassesJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new JobBuilder("expiresPassesJob", jobRepository)
				.start(expiresPassesStep(jobRepository, transactionManager))
				.build();
	}

	@Bean
	public Step expiresPassesStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("expiresPassesStep", jobRepository)
				.<PassEntity, PassEntity>chunk(CHUNK_SIZE, transactionManager)
				.reader(expirePassesItemReader())
				.processor(expirePassItemProcessor())
				.writer(expirePassesItemWriter())
				.build();
	}

	@Bean
	public JpaCursorItemReader<PassEntity> expirePassesItemReader() {
		return new JpaCursorItemReaderBuilder<PassEntity>()
				.name("expirePassesItemReader")
				.entityManagerFactory(entityManagerFactory)
				.queryString("select p from PassEntity p where p.status =:status and p.endedAt <= :endedAt")
				.parameterValues(Map.of("status", PassStatus.EXPIRED, "endedAt", LocalDateTime.now()))
				.build();
	}

	@Bean
	public ItemProcessor<PassEntity, PassEntity> expirePassItemProcessor() {
		return passEntity -> {
			passEntity.setStatus(PassStatus.EXPIRED);
			passEntity.setExpiredAt(LocalDateTime.now());
			return passEntity;
		};
	}

	@Bean
	public JpaItemWriter<PassEntity> expirePassesItemWriter() {
		return new JpaItemWriterBuilder<PassEntity>()
				.entityManagerFactory(entityManagerFactory)
				.build();
	}
}
