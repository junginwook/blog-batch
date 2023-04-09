package blog.study.top.job.blog.spring_batch에서_socket_was_closed_by_server_발생시;

import blog.study.top.repository.pay.PayEntity;
import blog.study.top.repository.product.Product;
import blog.study.top.repository.product.Store;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SocketCloseSlowProcessorBatch {
	private final JobRepository jobRepository;
	private final EntityManagerFactory entityManagerFactory;
	private final PlatformTransactionManager transactionManager;
	private final DataSource dataSource;

	private static final int chunkSize = 10;

	@Bean
	public Job job_230409_2() throws Exception {
		return new JobBuilder("job_230409_2", jobRepository)
				.start(step_230409_2())
				.build();
	}

	@Bean
	public Step step_230409_2() throws Exception {
		return new StepBuilder("step_230409_2", jobRepository)
				.<Product, Store>chunk(chunkSize, transactionManager)
				.reader(reader_230409_2(null))
				.processor(processor())
				.writer(writer_230409_2())
				.build();
	}

	@Bean
	@StepScope
	public JdbcPagingItemReader<Product> reader_230409_2(
			@Value("#{jobParameters[txDate]}") String txDate
	) throws Exception {

		Map<String, Object> params = new HashMap<>();
		params.put("txDate", txDate);

		SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
		queryProvider.setDataSource(dataSource);
		queryProvider.setSelectClause("id, name, price, create_date");
		queryProvider.setFromClause("from product");
		queryProvider.setWhereClause("where create_date = :txDate");
		queryProvider.setSortKey("id");

		return new JdbcPagingItemReaderBuilder<Product>()
				.name("reader_230409_2")
				.pageSize(chunkSize)
				.fetchSize(chunkSize)
				.dataSource(dataSource)
				.rowMapper((rs, rowNum) -> {
					return new Product(
							rs.getString("name"),
							rs.getInt("price"),
							rs.getObject("create_date", LocalDate.class)
					);
				})
				.queryProvider(queryProvider.getObject())
				.parameterValues(params)
				.build();
	}

	public ItemProcessor<Product, Store> processor() {
		return item -> {
			log.info("processor start");
			Thread.sleep(150_000);
			log.info("processor end");
			return new Store(item.getName(), item.getName());
		};
	}

	@Bean
	public JpaItemWriter<Store> writer_230409_2() {
		return new JpaItemWriterBuilder<Store>()
				.entityManagerFactory(entityManagerFactory)
				.build();
	}
}
