package blog.study.top.job.blog.spring_batch_paging_reader_사용시_같은_조건의_데이터를_읽고_수정할때_문제;

import static org.assertj.core.api.Assertions.assertThat;

import blog.study.top.job.blog.TestBatchConfig;
import blog.study.top.repository.pay.PayEntity;
import blog.study.top.repository.pay.PayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBatchTest
@SpringBootTest(classes = {
		PayPagingPageJobConfiguration.class,
		TestBatchConfig.class
})
class PayPagingPageJobConfigurationTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private PayRepository payRepository;

	@BeforeEach
	void setUp() {
		for (long i = 0; i < 50; i++) {
			payRepository.save(new PayEntity(i, false));
		}
	}

	@Test
	void testJob() throws Exception {
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
		assertThat(payRepository.findAllSuccess().size()).isEqualTo(50);
	}
}