package blog.study.top.job.blog.itemReader;

import blog.study.top.entity.Pay;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JdbcCursorItemReaderJobConfiguration {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final DataSource dataSource;
	private final int CHUNK_SIZE = 10;

	/**
	 * 커서 기반의 itemReader 는 하나의 커넥션으로 작업을 처리한다.
	 * 따라서 db 의 connection timeout 을 충분히 크게 설정해 주어야 한다.
	 */
	@Bean
	public Job jobCursorItemReaderJob() {
		return new JobBuilder("jobCursorItemReaderJob", jobRepository)
				.start(jdbcCursorItemReaderStep())
				.build();
	}

	@Bean
	public Step jdbcCursorItemReaderStep() {
		return new StepBuilder("jdbcCursorItemReaderStep", jobRepository)
				.<Pay, Pay>chunk(CHUNK_SIZE, transactionManager)
				.reader(jdbcCursorItemReader())
				.writer(jdbCursorItemWriter())
				.build();
	}

	@Bean
	public JdbcCursorItemReader<Pay> jdbcCursorItemReader() {
		return new JdbcCursorItemReaderBuilder<Pay>()
				.fetchSize(CHUNK_SIZE)
				.dataSource(dataSource)
				.rowMapper(new BeanPropertyRowMapper<>(Pay.class))
				.sql("SELECT id, amount, tx_name, tx_date_time FROM pay")
				.name("jdbcCursorItemReader")
				.build();
	}

	@Bean
	public ItemWriter<Pay> jdbCursorItemWriter() {
		return lists -> {
			for (Pay pay : lists) {
				log.info("Current Pay={}", pay);
			}
		};
	}
}
