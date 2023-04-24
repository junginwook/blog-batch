package blog.study.top.job.blog.spring_batch에서_1억건_데이터_배치업데이트;

import static org.assertj.core.api.Assertions.assertThat;

import blog.study.top.job.blog.TestBatchConfig;
import blog.study.top.job.blog.spring_batch와_QuerydslItemReader.QuerydslPagingAdvancedItemReader;
import blog.study.top.repository.pass.PassEntity;
import blog.study.top.repository.pass.PassRepository;
import blog.study.top.repository.pass.PassStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBatchTest
@SpringBootTest(classes = {
	TestBatchConfig.class,
	QuerydslPagingItemReader_230422.class}
)
class QuerydslPagingItemReader_통합_230422Test {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;
	@Autowired
	private PassRepository passRepository;

	public StepExecution getStepExecution() {
		JobParameters jobParameters = new JobParametersBuilder()
				.addLocalDate("createdAt", LocalDate.of(2023, 4, 23))
				.addLong("remainingCount", 10L)
				.toJobParameters();

		return MetaDataInstanceFactory.createStepExecution(jobParameters);
	}

	@BeforeEach
	void setUp() {
		passRepository.save(
				PassEntity.builder()
						.packageSeq(null)
						.userId("userId")
						.passStatus(PassStatus.EXPIRED)
						.remainingCount(20)
						.createdAt(LocalDateTime.of(2023, 4, 23, 0, 0, 0))
						.startedAt(LocalDateTime.now())
						.endedAt(LocalDateTime.now())
						.expiredAt(LocalDateTime.now())
						.build()
		);
		passRepository.save(
				PassEntity.builder()
						.packageSeq(null)
						.userId("userId")
						.passStatus(PassStatus.EXPIRED)
						.remainingCount(20)
						.createdAt(LocalDateTime.of(2023, 4, 23, 0, 0, 0))
						.startedAt(LocalDateTime.now())
						.endedAt(LocalDateTime.now())
						.expiredAt(LocalDateTime.now())
						.build()
		);
		passRepository.save(
				PassEntity.builder()
						.packageSeq(null)
						.userId("userId")
						.passStatus(PassStatus.EXPIRED)
						.remainingCount(20)
						.createdAt(LocalDateTime.of(2023, 4, 23, 0, 0, 0))
						.startedAt(LocalDateTime.now())
						.endedAt(LocalDateTime.now())
						.expiredAt(LocalDateTime.now())
						.build()
		);
		passRepository.save(
				PassEntity.builder()
						.packageSeq(null)
						.userId("userId")
						.passStatus(PassStatus.PROGRESSED)
						.remainingCount(20)
						.createdAt(LocalDateTime.of(2023, 4, 23, 0, 0, 0))
						.startedAt(LocalDateTime.now())
						.endedAt(LocalDateTime.now())
						.expiredAt(LocalDateTime.now())
						.build()
		);

		passRepository.save(
				PassEntity.builder()
						.packageSeq(null)
						.userId("userId")
						.passStatus(PassStatus.READY)
						.remainingCount(20)
						.createdAt(LocalDateTime.of(2023, 4, 23, 0, 0, 0))
						.startedAt(LocalDateTime.now())
						.endedAt(LocalDateTime.now())
						.expiredAt(LocalDateTime.now())
						.build()
		);
	}

	@Test
	void testJob() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
				.addLocalDate("createdAt", LocalDate.of(2023, 4, 23))
				.addLong("remainingCount", 10L)
				.toJobParameters();

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
		List<PassEntity> passEntityList = passRepository.findPassEntitiesByPassStatus(PassStatus.PROGRESSED);
		assertThat(passEntityList.size()).isEqualTo(4);
	}
}