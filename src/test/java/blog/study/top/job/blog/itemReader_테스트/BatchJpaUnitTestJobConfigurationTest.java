package blog.study.top.job.blog.itemReader_테스트;

import static org.assertj.core.api.Assertions.assertThat;

import blog.study.top.job.blog.TestBatchConfig;
import blog.study.top.repository.pay.PayEntity;
import blog.study.top.repository.pay.PayRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBatchTest
@SpringBootTest(classes = {
		TestBatchConfig.class,
		BatchJpaUnitTestJobConfiguration.class
})
class BatchJpaUnitTestJobConfigurationTest {

	@Autowired private JpaPagingItemReader<PayEntity> reader;
	@Autowired private PayRepository payRepository;

	public StepExecution getStepExecution() {
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("txName", "name")
				.toJobParameters();

		return MetaDataInstanceFactory.createStepExecution(jobParameters);
	}

	@AfterEach
	public void tearDown() {
		payRepository.deleteAllInBatch();
	}

	@Test
	void testItemReader() throws Exception {
		savePay(1L, "name");
		savePay(1L, "name");
		savePay(1L, "name2");

		//영속성 컨텍스트 시작
		reader.open(new ExecutionContext());

		assertThat(reader.read().getAmount()).isEqualTo(1L);
		assertThat(reader.read().getAmount()).isEqualTo(1L);
		assertThat(reader.read()).isNull();
	}

	private PayEntity savePay(Long amount, String txName) {
		return payRepository.save(new PayEntity(amount, txName, LocalDateTime.now()));
	}
}