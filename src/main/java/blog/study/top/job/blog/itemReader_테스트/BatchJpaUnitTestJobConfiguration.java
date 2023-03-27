package blog.study.top.job.blog.itemReader_테스트;

import blog.study.top.repository.pay.PayEntity;
import jakarta.persistence.EntityManagerFactory;
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
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchJpaUnitTestJobConfiguration {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

	private final EntityManagerFactory entityManagerFactory;
	private static final int chunkSize = 10;

	@Bean
	public Job batchJpaTestJob() {
		return new JobBuilder("batchJpaTestJob", jobRepository)
				.start(batchJpaTestStep())
				.build();
	}

	@Bean
	public Step batchJpaTestStep() {
		return new StepBuilder("batchJpaTestStep", jobRepository)
				.<PayEntity, PayEntity>chunk(chunkSize, transactionManager)
				.reader(batchJpaTestItemReader(null))
				.processor(batchItemProcessor())
				.writer(batchJpaItemWriter())
				.build();
	}

	/**
	 * stepScope 는 target Class bean 오브젝트를 상속 받은 프록시 객체를 생성한다.
	 */
	@Bean
	@StepScope
	public JpaPagingItemReader<PayEntity> batchJpaTestItemReader(
			@Value("#{jobParameters[txName]}") String txName
	) {
		Map<String, Object> params = new HashMap<>();
		params.put("txName", txName);

		return new JpaPagingItemReaderBuilder<PayEntity>()
				.pageSize(chunkSize)
				.parameterValues(params)
				.queryString("select p from pay p where p.txName = :txName")
				.name("batchJpaTestItemReader")
				.entityManagerFactory(entityManagerFactory)
				.build();
	}

	@Bean
	public ItemProcessor<PayEntity, PayEntity> batchItemProcessor() {
		return (item) -> {
			item.changeName("txName2");
			return item;
		};
	}

	@Bean
	public ItemWriter<PayEntity> batchJpaItemWriter() {
		return list -> {
			for(PayEntity payEntity : list) {
				System.out.println("payEntity: " + payEntity.getTxName());
			}
		};
	}
}
