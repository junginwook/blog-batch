package blog.study.top.job.blog.itemReader;

import blog.study.top.repository.pay.PayEntity;
import jakarta.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaPagingItemReaderJdbcConfiguration {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final EntityManagerFactory entityManagerFactory;
	private int CHUNK_SIZE = 3;

	@Bean
	public Job jpaPagingItemReaderJob() {
		return new JobBuilder("jpaPagingItemReaderJob", jobRepository)
				.start(jpaPagingItemReaderStep())
				.build();
	}

	@Bean
	public Step jpaPagingItemReaderStep() {
		return new StepBuilder("jpaPagingItemReaderStep", jobRepository)
				.<PayEntity, PayEntity>chunk(CHUNK_SIZE, transactionManager)
				.reader(jpaPagingItemReader())
				.writer(jpaPagingItemWriter())
				.build();
	}

	@Bean
	public JpaPagingItemReader<PayEntity> jpaPagingItemReader() {
		Map<String, Object> params = new HashMap<>();
		params.put("txName", "txName2");

		return new JpaPagingItemReaderBuilder<PayEntity>()
				.name("jpaPagingItemReader")
				.entityManagerFactory(entityManagerFactory)
				.pageSize(CHUNK_SIZE)
				.queryString("SELECT p FROM pay p WHERE p.txName = :txName AND amount >= 1")
				.parameterValues(params)
				.build();
	}

	@Bean
	public ItemWriter<PayEntity> jpaPagingItemWriter() {
		return list -> {
			for (PayEntity payEntity : list) {
				log.info("pay name={}", payEntity.getTxName());
			}
		};
	}
}
