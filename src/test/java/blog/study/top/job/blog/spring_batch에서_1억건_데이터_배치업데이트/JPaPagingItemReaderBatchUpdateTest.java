package blog.study.top.job.blog.spring_batch에서_1억건_데이터_배치업데이트;

import static org.assertj.core.api.Assertions.assertThat;

import blog.study.top.repository.pass.PassEntity;
import blog.study.top.repository.pass.PassRepository;
import blog.study.top.repository.pass.PassStatus;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(JPaPagingItemReaderBatchUpdate.class)
class JPaPagingItemReaderBatchUpdateTest extends AbstractTestBase {
	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;
	@Autowired
	private PassRepository passRepository;

	public StepExecution getStepExecution() {
		JobParameters jobParameters = new JobParametersBuilder()
				.addLocalDate("createdAt", LocalDate.of(2023, 4, 23))
				.toJobParameters();

		return MetaDataInstanceFactory.createStepExecution(jobParameters);
	}

	@Test
	void testJob() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
				.addLocalDate("createdAt", LocalDate.of(2023, 4, 23))
				.toJobParameters();

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
		List<PassEntity> passEntityList = passRepository.findPassEntitiesByPassStatus(PassStatus.EXPIRED);
		assertThat(passEntityList.size()).isEqualTo(10001);
	}
}