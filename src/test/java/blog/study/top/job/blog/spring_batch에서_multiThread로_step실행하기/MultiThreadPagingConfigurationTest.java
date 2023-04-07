package blog.study.top.job.blog.spring_batch에서_multiThread로_step실행하기;

import static org.assertj.core.api.Assertions.assertThat;

import blog.study.top.job.blog.TestBatchConfig;
import blog.study.top.repository.product.Product;
import blog.study.top.repository.product.ProductBackup;
import blog.study.top.repository.product.ProductStatus;
import blog.study.top.repository.product.repository.ProductBackupRepository;
import blog.study.top.repository.product.repository.ProductRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
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
		TestBatchConfig.class,
		MultiThreadPagingConfiguration.class
})
class MultiThreadPagingConfigurationTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ProductBackupRepository productBackupRepository;

	@AfterEach
	void tearDown() {
		productRepository.deleteAll();
		productBackupRepository.deleteAll();
	}

	@Test
	public void testJob() throws Exception {
		//given
		LocalDate createDate = LocalDate.of(2020, 4, 13);
		ProductStatus status = ProductStatus.APPROVE;
		long price = 1000L;

		for(int i = 0; i < 10; i++) {
			productRepository.save(
					new Product((int) (i * price), createDate, status)
			);
		}

		//when
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("createDate", createDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
				.addString("status", status.name())
				.toJobParameters();

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

		//then
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
		List<ProductBackup> backups = productBackupRepository.findAll();
		backups.sort(Comparator.comparingLong(ProductBackup::getPrice));

		assertThat(backups).hasSize(10);
		assertThat(backups.get(0).getPrice()).isEqualTo(0L);
		assertThat(backups.get(0).getPrice()).isEqualTo(9000L);
	}
}