package blog.study.top.job.blog.spring_batch와_QuerydslItemReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import blog.study.top.job.blog.TestBatchConfig;
import blog.study.top.repository.product.Product;
import blog.study.top.repository.product.repository.ProductBackupRepository;
import blog.study.top.repository.product.repository.ProductBatchRepository;
import blog.study.top.repository.product.repository.ProductRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBatchTest
@SpringBootTest(classes = {
		QuerydslPagingItemReaderConfiguration.class,
		TestBatchConfig.class
})
class QuerydslPagingItemReaderConfigurationTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductBackupRepository productBackupRepository;

	@Test
	void testJob() throws Exception {
		//given
		LocalDate txDate = LocalDate.of(2020, 10, 12);
		String name = "a";
		int categoryNo = 1;
		int expected1 = 1000;
		int expected2 = 2000;
		productRepository.save(new Product(name, expected1, categoryNo, txDate));
		productRepository.save(new Product(name, expected2, categoryNo, txDate));

		JobParameters jobParameters = new JobParametersBuilder()
				.addString("txDate", txDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
				.toJobParameters();
		//when
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

		//then
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
		assertThat(productBackupRepository.findAll().size()).isEqualTo(2);
	}
}