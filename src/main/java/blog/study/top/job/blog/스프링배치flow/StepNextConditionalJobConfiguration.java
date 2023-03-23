package blog.study.top.job.blog.스프링배치flow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StepNextConditionalJobConfiguration {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

	/**
	 * ExitStatus 입니다.
	 * Step의 실행 후 상태?
	 */
	@Bean
	public Job stepNextConditionalJob() {
		return new JobBuilder("stepNextConditionalJob", jobRepository)
				.start(conditionalJobStep1(null))
					.on("FAILED")
					.to(conditionalJobStep3())
					.on("*")
					.end()
				.from(conditionalJobStep1(null))
					.on("*")
					.to(conditionalJobStep2())
					.next(conditionalJobStep3())
					.on("*")
					.end()
				.end()
				.build();
	}

	@Bean
	@JobScope
	public Step conditionalJobStep1(@Value("#{jobParameters[requestDate]}") String requestDate) {
		return new StepBuilder("step1", jobRepository)
				.tasklet((contribution, chunkContext) -> {
					log.info(">>>>> requestDate is {}", requestDate);
					log.info(">>>>> This is stepNextConditionalJob Step1");
					contribution.setExitStatus(ExitStatus.FAILED);
					return RepeatStatus.FINISHED;
				}, transactionManager)
				.build();
	}

	@Bean
	public Step conditionalJobStep2() {
		return new StepBuilder("step2", jobRepository)
				.tasklet((contribution, chunkContext) -> {
					log.info(">>>>> This is stepNextConditionalJob Step2");
					return RepeatStatus.FINISHED;
				}, transactionManager)
				.build();
	}

	@Bean
	public Step conditionalJobStep3() {
		return new StepBuilder("step3", jobRepository)
				.tasklet((contribution, chunkContext) -> {
					log.info(">>>>> This is stepNextConditionalJob Step3");
					return RepeatStatus.FINISHED;
				}, transactionManager)
				.build();
	}
}
