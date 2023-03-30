package blog.study.top.job.blog.itemWriter에_list_전달하기;

import static org.assertj.core.api.Assertions.assertThat;

import blog.study.top.job.blog.TestBatchConfig;
import blog.study.top.repository.tax.Sales;
import blog.study.top.repository.tax.repository.SalesRepository;
import blog.study.top.repository.tax.repository.TaxRepository;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import java.util.Arrays;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBatchTest
@Import({TestBatchConfig.class,
		ItemListJobConfiguration.class
})
class ItemListJobConfigurationTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private SalesRepository salesRepository;

	@Autowired
	private TaxRepository taxRepository;

	@AfterEach
	public void tearDown() {
		salesRepository.deleteAllInBatch();
	}

	public StepExecution getStepExecution() {
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("txName", "name")
				.toJobParameters();

		return MetaDataInstanceFactory.createStepExecution(jobParameters);
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

	@Test
	void readerTest() {

	}
}