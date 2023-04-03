package blog.study.top.job.blog.itemReader;

import blog.study.top.repository.pay.PayEntity;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JdbcPagingItemReaderJobConfiguration {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final DataSource dataSource;
	private int CHUNK_SIZE = 2;

	/**
	 * pagingItemReader 는 page size 단위로 커넥션이 맺고 끊어진다.
	 */
	@Bean
	public Job jdbcPagingItemReaderJob() throws Exception {
		return new JobBuilder("jdbcPagingItemReaderJob", jobRepository)
				.start(jdbcPagingItemReaderStep())
				.build();
	}

	@Bean
	public Step jdbcPagingItemReaderStep() throws Exception {
		return new StepBuilder("jdbcPagingItemReaderStep", jobRepository)
				.<PayEntity, PayEntity>chunk(CHUNK_SIZE, transactionManager)
				.reader(jdbcPagingItemReader())
				.writer(jdbcPagingItemWriter())
				.build();
	}

	@Bean
	@JobScope
	public JdbcPagingItemReader<PayEntity> jdbcPagingItemReader() throws Exception {
		Map<String, Object> parameterValues = new HashMap<>();
		parameterValues.put("amount", 1);

		return new JdbcPagingItemReaderBuilder<PayEntity>()
				.pageSize(CHUNK_SIZE)
				.fetchSize(CHUNK_SIZE)
				.dataSource(dataSource)
				.rowMapper(new BeanPropertyRowMapper<>(PayEntity.class))
				.queryProvider(createQueryProvider())
				.parameterValues(parameterValues)
				.name("jdbcPagingItemReader")
				.build();
	}

	private ItemWriter<PayEntity> jdbcPagingItemWriter() {
		return lists -> {
			for (PayEntity payEntity : lists) {
				log.info("Cursor Pay={}", payEntity);
			}
		};
	}

	@Bean
	public PagingQueryProvider createQueryProvider() throws Exception {
		SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
		queryProvider.setDataSource(dataSource);
		queryProvider.setSelectClause("id, amount, tx_name, tx_date_time");
		queryProvider.setFromClause("from pay");
		queryProvider.setWhereClause("where amount >= :amount");

		Map<String, Order> sortKeys = new HashMap<>(1);
		sortKeys.put("id", Order.ASCENDING);

		queryProvider.setSortKeys(sortKeys);

		return queryProvider.getObject();
	}
}
