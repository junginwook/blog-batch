package blog.study.top.job.blog.itemWriter;

import static org.assertj.core.api.Assertions.assertThat;

import blog.study.top.job.blog.TestBatchConfig;
import blog.study.top.repository.pay.PayEntity;
import blog.study.top.repository.pay.PayRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBatchTest
@SpringBootTest(classes = {JdbcBatchItemWriterJobConfiguration.class, TestBatchConfig.class})
class JdbcBatchItemWriterJobConfigurationTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private PayRepository payRepository;

	@Test
	void testJob() throws Exception {
		payRepository.deleteAll();
		payRepository.save(new PayEntity(1L, "txName", LocalDateTime.now()));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

		List<PayEntity> payEntityList = payRepository.findAll();
		assertThat(payEntityList.size()).isEqualTo(2);
	}
}