package blog.study.top.job.blog.스프링배치flow;

import static org.junit.jupiter.api.Assertions.*;

import blog.study.top.job.blog.TestBatchConfig;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBatchTest
@SpringBootTest(classes = {DeciderJobConfiguration.class, TestBatchConfig.class})
class DeciderJobConfigurationTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Test
	void testJob() throws Exception {
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
	}
}