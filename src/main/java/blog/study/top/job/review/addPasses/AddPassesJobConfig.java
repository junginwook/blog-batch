package blog.study.top.job.review.addPasses;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class AddPassesJobConfig {
	private final AddPassesTasklet addPassesTasklet;

	@Bean
	public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new JobBuilder("myJob", jobRepository)
				.start(usePassesStep(null, jobRepository, transactionManager))
				.build();
	}

	@Bean
	@JobScope
	public Step usePassesStep(@Value("#{jobParameters[requestDate]}") String requestDate, JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("myStep", jobRepository)
				.tasklet(addPassesTasklet, transactionManager)
				.build();
	}
}
