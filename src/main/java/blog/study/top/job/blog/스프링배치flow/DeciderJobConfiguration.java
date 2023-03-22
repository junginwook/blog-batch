package blog.study.top.job.blog.스프링배치flow;

import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DeciderJobConfiguration {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

	@Bean
	public Job deciderJob() {
		return new JobBuilder("deciderJob", jobRepository)
				.start(startStep())
				.next(decider())
				.from(decider())
					.on("ODD")
					.to(oddStep())
				.from(decider())
					.on("EVEN")
					.to(evenStep())
				.end().build();
	}

	@Bean
	public Step startStep() {
		return new StepBuilder("startStep", jobRepository)
				.tasklet((contribution, chunkContext) -> {
					log.info(">>>>> Start");
					return RepeatStatus.FINISHED;
				}, transactionManager)
				.build();
	}

	@Bean
	public Step evenStep() {
		return new StepBuilder("evenStep", jobRepository)
				.tasklet((contribution, chunkContext) -> {
					log.info(">>>>> 짝수입니다.");
					return RepeatStatus.FINISHED;
				}, transactionManager)
				.build();
	}

	@Bean
	public Step oddStep() {
		return new StepBuilder("oddStep", jobRepository)
				.tasklet((contribution, chunkContext) -> {
					log.info(">>>>> 홀수입니다.");
					return RepeatStatus.FINISHED;
				}, transactionManager)
				.build();
	}

	@Bean
	public JobExecutionDecider decider() {
		return new OddDecider();
	}

	private class OddDecider implements JobExecutionDecider {

		@Override
		public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
			Random rand = new Random();

			int randomNumber = rand.nextInt(50) + 1;
			log.info("랜덤숫자: {}", randomNumber);

			if(randomNumber % 2 == 0) {
				return new FlowExecutionStatus("EVEN");
			} else {
				return new FlowExecutionStatus("ODD");
			}
		}
	}
}
