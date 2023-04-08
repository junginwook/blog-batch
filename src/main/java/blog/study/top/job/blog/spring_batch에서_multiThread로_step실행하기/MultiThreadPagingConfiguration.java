package blog.study.top.job.blog.spring_batch에서_multiThread로_step실행하기;

import blog.study.top.repository.product.Product;
import blog.study.top.repository.product.ProductBackup;
import jakarta.persistence.EntityManagerFactory;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class MultiThreadPagingConfiguration {

	public static final String JOB_NAME = "multiThreadPagingBatch";
	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final EntityManagerFactory entityManagerFactory;
	private final DataSource readerDataSource;
	private final DataSource dataSource;
	private static final int chunkSize = 1;
	private int poolSize = 2;

	@Bean(name = JOB_NAME + "taskPool")
	public TaskExecutor executor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(poolSize);
		executor.setMaxPoolSize(poolSize);
		executor.setThreadNamePrefix("multi-thread-");
		executor.setWaitForTasksToCompleteOnShutdown(Boolean.TRUE);
		executor.initialize();
		return executor;
	}

	@Bean(name = JOB_NAME)
	public Job job() throws Exception {
		return new JobBuilder(JOB_NAME, jobRepository)
				.start(step())
				.build();
	}

	@Bean(name = JOB_NAME + "_step")
	public Step step() throws Exception {
		return new StepBuilder(JOB_NAME, jobRepository)
				.<Product, ProductBackup>chunk(chunkSize, transactionManager)
				.reader(reader(null))
				.processor(processor())
				.writer(writer())
				.taskExecutor(executor())
				.build();
	}

	@Bean(name = JOB_NAME + "_reader")
	@StepScope
	public JdbcPagingItemReader<Product> reader(
			@Value("#{jobParameters[createDate]}") String createDate
	) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("createDate", createDate);

		SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
		queryProvider.setDataSource(readerDataSource);
		queryProvider.setSelectClause("id, name, price, create_date");
		queryProvider.setFromClause("from product");
		queryProvider.setWhereClause("where create_date = :createDate");
		queryProvider.setSortKey("id");

		return new JdbcPagingItemReaderBuilder<Product>()
				.name(JOB_NAME + "_reader")
				.pageSize(chunkSize)
				.rowMapper(new RowMapper<Product>() {
					@Override
					public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
						return new Product(
								rs.getString("name"),
								rs.getLong("price"),
								rs.getObject("create_date", LocalDate.class)
						);
					}
				})
				.dataSource(readerDataSource)
				.queryProvider(queryProvider.getObject())
				.parameterValues(params)
				.saveState(false)
				.build();
	}

	private ItemProcessor<Product, ProductBackup> processor() {
		return ProductBackup::new;
	}

	@Bean(name = JOB_NAME + "_writer")
	@StepScope
	public JpaItemWriter<ProductBackup> writer() {
		return new JpaItemWriterBuilder<ProductBackup>()
				.entityManagerFactory(entityManagerFactory)
				.build();
	}
}
