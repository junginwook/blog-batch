package blog.study.top.job.blog.spring_batch에서_ItemReader를_mock객체로_교체하기;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import blog.study.top.job.blog.TestBatchConfig;
import blog.study.top.repository.customer.Customer;
import blog.study.top.repository.customer.CustomerBackupRepository;
import blog.study.top.repository.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.PlatformTransactionManager;

@SpringBatchTest
@SpringBootTest(classes = {
		CustomerCopyBatchConfiguration.class,
		TestBatchConfig.class
})
class CustomerCopyBatchConfigurationTest {

	@Autowired private JobLauncherTestUtils jobLauncherTestUtils;
	@Autowired private CustomerRepository customerRepository;
	@Autowired private CustomerBackupRepository customerBackupRepository;
	@MockBean(name = "reader230401")
	private JpaPagingItemReader<Customer> reader230401;

	@Test
	void testCopyBatchJob() throws Exception {
		given(reader230401.read()).willReturn(new Customer(1L), new Customer(2L), null);

		JobParametersBuilder builder = new JobParametersBuilder();
		builder.addString("mailHost", "gmail.com");
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(builder.toJobParameters());

		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
		assertThat(customerBackupRepository.findAll().size()).isEqualTo(2);
	}
}