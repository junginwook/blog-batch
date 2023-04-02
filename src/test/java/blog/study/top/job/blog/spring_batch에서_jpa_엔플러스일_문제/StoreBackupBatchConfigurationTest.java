package blog.study.top.job.blog.spring_batch에서_jpa_엔플러스일_문제;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import blog.study.top.job.blog.TestBatchConfig;
import blog.study.top.repository.product.Employee;
import blog.study.top.repository.product.Product;
import blog.study.top.repository.product.Store;
import blog.study.top.repository.product.repository.StoreHistoryRepository;
import blog.study.top.repository.product.repository.StoreRepository;
import java.time.LocalDate;
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

@SpringBatchTest
@SpringBootTest(classes = {
		StoreBackupBatchConfiguration.class,
		TestBatchConfig.class
})
class StoreBackupBatchConfigurationTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;
	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private StoreHistoryRepository storeHistoryRepository;

	@BeforeEach
	void setUp() {
		Store store1 = new Store("서점", "서울시 강남구");
		store1.addProduct(new Product("책1_1", 10000L));
		store1.addProduct(new Product("책1_2", 20000L));
		store1.addEmployee(new Employee("직원1", LocalDate.now()));
		storeRepository.save(store1);

		Store store2 = new Store("서점2", "서울시 강남구");
		store2.addProduct(new Product("책2_1", 10000L));
		store2.addProduct(new Product("책2_2", 20000L));
		store2.addEmployee(new Employee("직원2", LocalDate.now()));
		storeRepository.save(store2);

		Store store3 = new Store("서점3", "서울시 강남구");
		store3.addProduct(new Product("책3_1", 10000L));
		store3.addProduct(new Product("책3_2", 20000L));
		store3.addEmployee(new Employee("직원3", LocalDate.now()));
		storeRepository.save(store3);
	}

	@Test
	void testJob() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("address", "서울")
				.toJobParameters();

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
	}
}