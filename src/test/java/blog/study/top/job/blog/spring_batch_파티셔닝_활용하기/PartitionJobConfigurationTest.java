package blog.study.top.job.blog.spring_batch_파티셔닝_활용하기;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import blog.study.top.job.blog.TestBatchConfig;
import blog.study.top.repository.product.Product;
import blog.study.top.repository.product.ProductBackup;
import blog.study.top.repository.product.ProductStatus;
import blog.study.top.repository.product.repository.ProductBackupRepository;
import blog.study.top.repository.product.repository.ProductBatchRepository;
import blog.study.top.repository.product.repository.ProductRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBatchTest
@SpringBootTest(classes = {
		ProductBatchRepository.class,
		TestBatchConfig.class,
		PartitionJobConfiguration.class
})
class PartitionJobConfigurationTest {

	@Autowired
	private ProductRepository productRepository;


	@Autowired
	private ProductBackupRepository productBackupRepository;

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@AfterEach
	public void after() {
		productRepository.deleteAllInBatch();
		productBackupRepository.deleteAllInBatch();
	}

	@Test
	void testJob() throws Exception {
		//given
		LocalDate txDate = LocalDate.of(2021, 1, 12);

		ArrayList<Product> products = new ArrayList<>();
		int expectedCount = 50;
		for(int i = 1; i <= expectedCount; i++) {
			products.add(new Product(i, txDate, ProductStatus.APPROVE));
		}
		productRepository.saveAll(products);

		JobParameters jobParameters = jobLauncherTestUtils.getUniqueJobParametersBuilder()
				.addString("startDate", txDate.format(FORMATTER))
				.addString("endDate", txDate.plusDays(1).format(FORMATTER))
				.toJobParameters();

		//when
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

		//then
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
		List<ProductBackup> backups = productBackupRepository.findAll();
		assertThat(backups.size()).isEqualTo(expectedCount);

		List<Map<String, Object>> metaTable = jdbcTemplate.queryForList("select step_name, status, commit_count, read_count, write_count from BATCH_STEP_EXECUTION");
		for(Map<String, Object> step : metaTable) {
			System.out.println("row=" + step);
		}
	}
}