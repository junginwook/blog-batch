package blog.study.top.job.blog.itemReader_테스트;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

import blog.study.top.job.blog.TestBatchConfig;
import blog.study.top.repository.pay.PayEntity;
import java.time.LocalDate;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * H2 내장 데이터베이스 활용하여 테스트
 */
@Import({TestBatchConfig.class})
class BatchOnlyJdbcReaderTestConfigurationTest {

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	private ConfigurableApplicationContext context;
	private String txName;
	private BatchJdbcTestConfiguration job;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@BeforeEach
	public void setUp() {
		this.context = new AnnotationConfigApplicationContext(BatchPayDatabaseConfig.class);
		this.dataSource = (DataSource) context.getBean("dataSource");
		this.jdbcTemplate = new JdbcTemplate(this.dataSource);
		this.txName = "name";
		this.job = new BatchJdbcTestConfiguration(jobRepository, dataSource, transactionManager);
		this.job.setChunkSize(10);
	}

	@AfterEach
	public void tearDown() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	void testJob() throws Exception {
		jdbcTemplate.update("INSERT INTO pay (amount, tx_date_time, tx_name) VALUES(1, '2023-03-26 14:31:17.774606', 'name')");
		jdbcTemplate.update("INSERT INTO pay (amount, tx_date_time, tx_name) VALUES(1, '2023-03-26 14:31:17.774606', 'name')");
		jdbcTemplate.update("INSERT INTO pay (amount, tx_date_time, tx_name) VALUES(1, '2023-03-26 14:31:17.774606', 'name')");
		jdbcTemplate.update("INSERT INTO pay (amount, tx_date_time, tx_name) VALUES(1, '2023-03-26 14:31:17.774606', 'name')");
		jdbcTemplate.update("INSERT INTO pay (amount, tx_date_time, tx_name) VALUES(1, '2023-03-26 14:31:17.774606', 'name')");
		jdbcTemplate.update("INSERT INTO pay (amount, tx_date_time, tx_name) VALUES(1, '2023-03-26 14:31:17.774606', 'name')");
		jdbcTemplate.update("INSERT INTO pay (amount, tx_date_time, tx_name) VALUES(1, '2023-03-26 14:31:17.774606', 'name')");
		jdbcTemplate.update("INSERT INTO pay (amount, tx_date_time, tx_name) VALUES(1, '2023-03-26 14:31:17.774606', 'name')");
		jdbcTemplate.update("INSERT INTO pay (amount, tx_date_time, tx_name) VALUES(1, '2023-03-26 14:31:17.774606', 'name')");
		jdbcTemplate.update("INSERT INTO pay (amount, tx_date_time, tx_name) VALUES(1, '2023-03-26 14:31:17.774606', 'name')");
		jdbcTemplate.update("INSERT INTO pay (amount, tx_date_time, tx_name) VALUES(1, '2023-03-26 14:31:17.774606', 'name')");

		JdbcPagingItemReader<PayEntity> reader = job.batchJdbcUnitTestJobReader(txName);
		reader.afterPropertiesSet();

		assertThat(reader.read().getAmount()).isEqualTo(1L);
		assertThat(reader.read().getAmount()).isEqualTo(1L);
		assertThat(reader.read().getAmount()).isEqualTo(1L);
		assertThat(reader.read().getAmount()).isEqualTo(1L);
		assertThat(reader.read().getAmount()).isEqualTo(1L);
		assertThat(reader.read().getAmount()).isEqualTo(1L);
		assertThat(reader.read().getAmount()).isEqualTo(1L);
		assertThat(reader.read().getAmount()).isEqualTo(1L);
		assertThat(reader.read().getAmount()).isEqualTo(1L);
		assertThat(reader.read().getAmount()).isEqualTo(1L);
		assertThat(reader.read().getAmount()).isEqualTo(1L);
		assertThat(reader.read()).isNull();
	}
}