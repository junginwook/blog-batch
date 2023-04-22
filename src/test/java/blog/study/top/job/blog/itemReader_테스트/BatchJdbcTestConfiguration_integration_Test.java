package blog.study.top.job.blog.itemReader_테스트;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import blog.study.top.job.blog.TestBatchConfig;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBatchTest
@SpringBootTest(classes = {
		TestBatchConfig.class,
		BatchJdbcTestConfiguration.class
})
class BatchJdbcTestConfiguration_integration_Test {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private DataSource dataSource;

	private JdbcOperations jdbcTemplate;

	@BeforeEach
	public void setUp() throws Exception {
		this.jdbcTemplate = new JdbcTemplate(this.dataSource);
	}

	@Test
	void testJob() throws Exception {
		jdbcTemplate.update("INSERT INTO pay (amount, tx_date_time, tx_name, success_status) VALUES(1, '2023-03-26 14:31:17.774606', 'name', true)");
		jdbcTemplate.update("INSERT INTO pay (amount, tx_date_time, tx_name, success_status) VALUES(1, '2023-03-26 14:31:17.774606', 'name', true)");
		jdbcTemplate.update("INSERT INTO pay (amount, tx_date_time, tx_name, success_status) VALUES(1, '2023-03-26 14:31:17.774606', 'name2',true)");

		JobParameters jobParameters = new JobParametersBuilder()
				.addString("txName", "name")
				.toJobParameters();

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
	}
}