package blog.study.top.job.blog.itemReader_테스트;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.*;

import blog.study.top.job.blog.TestBatchConfig;
import blog.study.top.repository.pay.PayEntity;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@SpringBatchTest
@EnableAutoConfiguration
@SpringBootTest(classes = {
		TestBatchConfig.class,
		BatchJdbcTestConfiguration.class,
})
class BatchJdbcUnitTestJobConfigurationLegacyTest {

	@Autowired private JdbcPagingItemReader<PayEntity> reader;
	@Autowired private DataSource dataSource;
	private JdbcOperations jdbcTemplate;
	private String txName = "name";

	public StepExecution getStepExecution() {
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("txName", txName)
				.toJobParameters();

		return MetaDataInstanceFactory.createStepExecution(jobParameters);
	}

	@BeforeEach
	public void setUp() throws Exception {
		this.reader.setDataSource(dataSource);
		this.jdbcTemplate = new JdbcTemplate(this.dataSource);
	}

	@DisplayName("StepScope 가 필요한 테스트")
	@Test
	void testJob() throws Exception {
		jdbcTemplate.update("INSERT INTO pay (amount, tx_date_time, tx_name) VALUES(1, '2023-03-26 14:31:17.774606', 'name')");
		jdbcTemplate.update("INSERT INTO pay (amount, tx_date_time, tx_name) VALUES(1, '2023-03-26 14:31:17.774606', 'name')");
		jdbcTemplate.update("INSERT INTO pay (amount, tx_date_time, tx_name) VALUES(1, '2023-03-26 14:31:17.774606', 'name2')");

		assertThat(reader.read().getAmount()).isEqualTo(1L);
		assertThat(reader.read().getAmount()).isEqualTo(1L);
		assertThat(reader.read()).isNull();
	}
}