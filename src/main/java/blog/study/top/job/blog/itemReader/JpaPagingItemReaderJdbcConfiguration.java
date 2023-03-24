package blog.study.top.job.blog.itemReader;

import blog.study.top.entity.Pay;
import jakarta.persistence.EntityManagerFactory;
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
	private int CHUNK_SIZE = 10;

	@Bean
	public Job jpaPagingItemReaderJob() {
		return new JobBuilder("jpaPagingItemReaderJob", jobRepository)
				.start(jpaPagingItemReaderStep())
				.build();
	}

	@Bean
	public Step jpaPagingItemReaderStep() {
		return new StepBuilder("jpaPagingItemReaderStep", jobRepository)
				.<Pay, Pay>chunk(CHUNK_SIZE, transactionManager)
				.reader(jpaPagingItemReader())
				.writer(jpaPagingItemWriter())
				.build();
	}

	@Bean
	public JpaPagingItemReader<Pay> jpaPagingItemReader() {
		return new JpaPagingItemReaderBuilder<Pay>()
				.name("jpaPagingItemReader")
				.entityManagerFactory(entityManagerFactory)
				.pageSize(CHUNK_SIZE)
				.queryString("SELECT p FROM Pay p WHERE amount >= 2000")
				.build();
	}

	@Bean
	public ItemWriter<Pay> jpaPagingItemWriter() {
		return list -> {
			for (Pay pay: list) {
				log.info("pay name={}", pay.getTxName());
			}
		};
	}
}
