package blog.study.top.job.blog.itemWriter;

import blog.study.top.repository.pay.PayEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JdbcBatchItemWriterJobConfiguration {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private int CHUNK_SIZE = 10;
	private final DataSource dataSource;

	@Bean
	public Job jdbcBatchItemWriterJob() {
		return new JobBuilder("jdbcBatchItemWriterJob", jobRepository)
				.start(jdbcBatchItemWriterStep())
				.build();
	}

	@Bean
	public Step jdbcBatchItemWriterStep() {
		return new StepBuilder("jdbcBatchItemWriterStep", jobRepository)
				.<PayEntity, PayEntity>chunk(CHUNK_SIZE, transactionManager)
				.reader(jdbcBatchItemWriterReader())
				.processor(jdbcBatchItemProcessor())
				.writer(jdbcBatchItemWriter())
				.build();
	}

	@Bean
	public JdbcCursorItemReader<PayEntity> jdbcBatchItemWriterReader() {
		BeanPropertyRowMapper<PayEntity> rowMapper = new BeanPropertyRowMapper<>(PayEntity.class);
		rowMapper.setPrimitivesDefaultedForNullValue(true);

		return new JdbcCursorItemReaderBuilder<PayEntity>()
				.fetchSize(CHUNK_SIZE)
				.dataSource(dataSource)
				.rowMapper(getRowMapper())
				.sql("SELECT id, amount, tx_name, tx_date_time FROM pay")
				.name("jdbcBatchItemWriter")
				.build();
	}

	@Bean
	public ItemProcessor<PayEntity, PayEntity> jdbcBatchItemProcessor() {
		return item -> {
			System.out.println("id: " + item.getId());
			System.out.println("txName: " + item.getTxName());
			System.out.println("amount: " + item.getAmount());
			System.out.println("txDateTime: " + item.getTxDateTime());
			return item;
		};
	}

	@Bean
	public JdbcBatchItemWriter<PayEntity> jdbcBatchItemWriter() {
		return new JdbcBatchItemWriterBuilder<PayEntity>()
				.dataSource(dataSource)
				.beanMapped()
				.sql("insert into pay(amount, tx_name, tx_date_time) values (:amount, :txName, :txDateTime)")
				.build();
	}

	private RowMapper<PayEntity> getRowMapper() {
		return (rs, rowNum) -> {
			PayEntity payEntity = new PayEntity(
					rs.getLong("amount"),
					rs.getString("tx_name"),
					rs.getObject("tx_date_time", LocalDateTime.class)
			);
			return payEntity;
		};
	}
}
