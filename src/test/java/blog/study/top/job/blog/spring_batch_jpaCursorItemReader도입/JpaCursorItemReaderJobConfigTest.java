package blog.study.top.job.blog.spring_batch_jpaCursorItemReader도입;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import blog.study.top.job.blog.TestBatchConfig;
import blog.study.top.repository.pay.PayEntity;
import blog.study.top.repository.pay.PayRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBatchTest
@SpringBootTest(classes = {
		TestBatchConfig.class,
		JpaCursorItemReaderJobConfig.class
})
class JpaCursorItemReaderJobConfigTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private PayRepository payRepository;

	@AfterEach
	void tearDown() {
		payRepository.deleteAll();
	}

	@Test
	void testJob() throws Exception {
		//given
		for(long i = 0; i < 10; i++) {
			payRepository.save(new PayEntity(i*1000, String.valueOf(i), LocalDateTime.now()));
		}

		JobParameters jobParameters = jobLauncherTestUtils.getUniqueJobParametersBuilder()
				.addString("version", "1")
				.toJobParameters();

		//when
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

		//then
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
	}
}