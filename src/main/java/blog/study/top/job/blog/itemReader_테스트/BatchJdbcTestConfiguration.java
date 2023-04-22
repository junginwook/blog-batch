package blog.study.top.job.blog.itemReader_테스트;

import blog.study.top.repository.pay.PayEntity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchJdbcTestConfiguration {
	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static final String JOB_NAME = "batchJdbcUnitTestJob";

	private final JobRepository jobRepository;
	private final DataSource dataSource;
	private final PlatformTransactionManager transactionManager;
	private int chunkSize;

	@Value("${spring.batch.chunk-size:1000}")
	public void setChunkSize(int chunkSize) {
		this.chunkSize = chunkSize;
	}

	@Bean
	public Job batchJdbcUnitTestJob() throws Exception {
		return new JobBuilder("batchJob", jobRepository)
				.start(batchJdbcUnitTestJobStep())
				.build();
	}

	@Bean
	public Step batchJdbcUnitTestJobStep() throws Exception {
		return new StepBuilder("batchStep", jobRepository)
				.<PayEntity, PayEntity>chunk(chunkSize, transactionManager)
				.reader(batchJdbcUnitTestJobReader(null))
				.writer(batchJdbcUnitTestJobWriter())
				.build();
	}

	@Bean
	@StepScope
	public JdbcPagingItemReader<PayEntity> batchJdbcUnitTestJobReader(
			@Value("#{jobParameters[txName]}") String txName
	) throws Exception {

		Map<String, Object> params = new HashMap<>();
		params.put("txName", txName);

		SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
		queryProvider.setDataSource(dataSource);
		queryProvider.setSelectClause("id, amount, tx_name, tx_date_time");
		queryProvider.setFromClause("from pay");
		queryProvider.setWhereClause("where tx_name = :txName");
		queryProvider.setSortKey("id");

		return new JdbcPagingItemReaderBuilder<PayEntity>()
				.name("batchJdbcUnitTestJobReader")
				.pageSize(chunkSize)
				.fetchSize(chunkSize)
				.dataSource(dataSource)
				.rowMapper((rs, rowNum) -> {
					return new PayEntity(
							rs.getLong("amount"),
							rs.getString("tx_name"),
							rs.getObject("tx_date_time", LocalDateTime.class)
					);
				})
				.queryProvider(queryProvider.getObject())
				.parameterValues(params)
				.build();
	}

	@Bean
	public JdbcBatchItemWriter<PayEntity> batchJdbcUnitTestJobWriter() {
		return new JdbcBatchItemWriterBuilder<PayEntity>()
				.dataSource(dataSource)
				.sql("insert into pay(amount, tx_name, tx_date_time, success_status) values (:amount, :txName, :txDateTime, true)")
				.beanMapped()
				.build();
	}

}
