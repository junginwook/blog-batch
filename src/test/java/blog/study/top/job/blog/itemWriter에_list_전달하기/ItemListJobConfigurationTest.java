package blog.study.top.job.blog.itemWriter에_list_전달하기;

import static org.assertj.core.api.Assertions.assertThat;

import blog.study.top.job.blog.TestBatchConfig;
import blog.study.top.repository.tax.Sales;
import blog.study.top.repository.tax.repository.SalesRepository;
import blog.study.top.repository.tax.repository.TaxRepository;
import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBatchTest
@SpringBootTest(classes = {TestBatchConfig.class,
		ItemListJobConfiguration.class
})
class ItemListJobConfigurationTest {
	@Autowired private JobLauncherTestUtils jobLauncherTestUtils;
	@Autowired private SalesRepository salesRepository;
	@Autowired private TaxRepository taxRepository;

	@AfterEach
	public void tearDown() {
		salesRepository.deleteAllInBatch();
	}

	@Test
	public void jobTest() throws Exception {
		salesRepository.saveAll(Arrays.asList(
			new Sales(10000L, 1L),
			new Sales(20000L, 2L),
			new Sales(30000L, 3L)
		));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
		assertThat(taxRepository.findAll().size()).isEqualTo(9);
	}
}