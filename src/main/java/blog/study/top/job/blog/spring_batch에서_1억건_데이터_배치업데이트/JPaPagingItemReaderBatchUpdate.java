package blog.study.top.job.blog.spring_batch에서_1억건_데이터_배치업데이트;

import blog.study.top.repository.pass.PassEntity;
import blog.study.top.repository.pass.PassStatus;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JPaPagingItemReaderBatchUpdate {

	private final JobRepository jobRepository;
	private final EntityManagerFactory entityManagerFactory;
	private final PlatformTransactionManager transactionManager;
	private final int chunkSize = 2000;

	@Bean
	public Job job_230423_1() {
		return new JobBuilder("job_230423_1", jobRepository)
				.start(step_230423_1())
				.build();
	}

	@Bean
	public Step step_230423_1() {
		return new StepBuilder("step_230423_1", jobRepository)
				.<PassEntity, PassEntity>chunk(chunkSize, transactionManager)
				.reader(reader_230423_1(null))
				.processor(itemProcessor())
				.writer(writer_230423_1())
				.build();
	}

	@Bean
	@StepScope
	public JpaPagingItemReader<PassEntity> reader_230423_1(
			@Value("#{jobParameters[createdAt]}") LocalDate createdAt
	) {
		Map<String, Object> map = new HashMap<>();
		map.put("createdAt", createdAt.atTime(0, 0, 0));
		map.put("passStatus", PassStatus.PROGRESSED);

		return new JpaPagingItemReaderBuilder<PassEntity>()
				.name("reader_230423_1")
				.pageSize(chunkSize)
				.queryString("SELECT p from PassEntity p WHERE p.createdAt =:createdAt AND p.passStatus =:passStatus")
				.parameterValues(map)
				.entityManagerFactory(entityManagerFactory)
				.build();
	}

	public ItemProcessor<PassEntity, PassEntity> itemProcessor() {
		return item -> {
			item.setPassStatus(PassStatus.EXPIRED);
			return item;
		};
	}

	public ItemWriter<PassEntity> writer_230423_1() {
		return list -> {
			log.info("list size: {}", list.size());
		};
	}
}
