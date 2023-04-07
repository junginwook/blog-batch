package blog.study.top.job.blog.spring_batch_paging_reader_사용시_같은_조건의_데이터를_읽고_수정할때_문제;

import blog.study.top.job.blog.config.UniqueRunIdIncrementer;
import blog.study.top.repository.pay.PayEntity;
import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PayPagingCursorJobConfiguration {

	private final JobRepository jobRepository;
	private final EntityManagerFactory entityManagerFactory;
	private final PlatformTransactionManager transactionManager;
	private final DataSource dataSource;
	private final static int chunkSize = 10;

	@Bean
	public Job job230402() {
		return new JobBuilder("job230402", jobRepository)
				.preventRestart()
				.start(step230402(null))
				.build();
	}

	@Bean
	@JobScope
	public Step step230402(
			@Value("#{jobParameters[requestDate]}") String requestDate
	) {

		System.out.println("requestDate: " + requestDate);
		if (requestDate.equals("20230130")) {
			throw new IllegalArgumentException("잘못된 값입니다.");
		}

		return new StepBuilder("step230402", jobRepository)
				.<PayEntity, PayEntity>chunk(chunkSize, transactionManager)
				.reader(reader230402())
				.processor(processor230402())
				.writer(writer230402())
				.build();
	}

	/**
	 * 같은 조건의 데이터를 읽고 수정할 시 update 할때마다 조회 데이터의 숫자가 계속 줄어들기 때문에
	 * page 의 값을 0으로 고정하는 방법이 있다.
	 * 또는 cursor 방식으로 데이터를 읽으면 된다.
	 */
	@Bean
	@StepScope
	public JdbcCursorItemReader<PayEntity> reader230402() {
		return new JdbcCursorItemReaderBuilder<PayEntity>()
				.sql("SELECT * FROM pay p WHERE p.success_status = false")
				.rowMapper(new BeanPropertyRowMapper<>(PayEntity.class))
				.fetchSize(chunkSize)
				.dataSource(dataSource)
				.name("payPagingReader")
				.build();
	}

	@Bean
	@StepScope
	public ItemProcessor<PayEntity, PayEntity> processor230402() {
		return item -> {
			item.success();
			return item;
		};
	}

	@Bean
	@StepScope
	public JpaItemWriter<PayEntity> writer230402() {
		return new JpaItemWriterBuilder<PayEntity>()
				.entityManagerFactory(entityManagerFactory)
				.build();
	}
}
