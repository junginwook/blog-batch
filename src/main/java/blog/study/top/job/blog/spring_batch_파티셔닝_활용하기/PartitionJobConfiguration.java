package blog.study.top.job.blog.spring_batch_파티셔닝_활용하기;

import blog.study.top.repository.product.Product;
import blog.study.top.repository.product.ProductBackup;
import blog.study.top.repository.product.repository.ProductBackupRepository;
import blog.study.top.repository.product.repository.ProductRepository;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class PartitionJobConfiguration {

	private final ProductBackupRepository productBackupRepository;

	private final ProductRepository productRepository;

	private final JobRepository jobRepository;
	private final EntityManagerFactory entityManagerFactory;
	private final PlatformTransactionManager transactionManager;
	private static int poolSize = 10;
	private static int chunkSize = 10;

	@Bean
	public TaskExecutorPartitionHandler partitionHandler() {
		TaskExecutorPartitionHandler partitionHandler = new TaskExecutorPartitionHandler();
		partitionHandler.setStep(step1());
		partitionHandler.setTaskExecutor(executor_230410());
		partitionHandler.setGridSize(poolSize);
		return partitionHandler;
	}

	@Bean
	public TaskExecutor executor_230410() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(poolSize);
		executor.setMaxPoolSize(poolSize);
		executor.setThreadNamePrefix("partition-thread");
		executor.setWaitForTasksToCompleteOnShutdown(Boolean.TRUE);
		executor.initialize();
		return executor;
	}

	public Job job_230410() {
		return new JobBuilder("job_230410", jobRepository)
				.start(stepManager())
				.build();
	}

	@Bean
	public Step stepManager() {
		return new StepBuilder("stepManager", jobRepository)
				.partitioner("step1", partitioner(null, null))
				.step(step1())
				.partitionHandler(partitionHandler())
				.build();
	}

	@Bean
	public Step step1() {
		return new StepBuilder("step1", jobRepository)
				.<Product, ProductBackup>chunk(chunkSize, transactionManager)
				.reader(reader_230410(null, null))
				.processor(processor())
				.writer(writer_230410(null, null))
				.build();
	}

	@Bean
	@StepScope
	public ProductIdRangePartitioner partitioner(
			@Value("#{jobParameters[startDate]}") String startDate,
			@Value("#{jobParameters[endDate]}") String endDate
	) {
		LocalDate startLocalDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		LocalDate endLocalDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		return new ProductIdRangePartitioner(productRepository, startLocalDate, endLocalDate);
	}

	@Bean
	@StepScope
	public JpaPagingItemReader<Product> reader_230410(
			@Value("#{stepExecutionContext[minId]}") Long minId,
			@Value("#{stepExecutionContext[maxId]}") Long maxId
	) {
		Map<String, Object> params = new HashMap<>();
		params.put("minId", minId);
		params.put("maxId", maxId);

		log.info("reader minId={}, maxId={}", maxId, maxId);

		return new JpaPagingItemReaderBuilder<Product>()
				.name("reader_230410")
				.entityManagerFactory(entityManagerFactory)
				.pageSize(chunkSize)
				.queryString(
						"SELECT p " +
						"FROM Product p " +
						"WHERE p.id BETWEEN :minId AND :maxId")
				.parameterValues(params)
				.build();
	}

	public ItemProcessor<Product, ProductBackup> processor() {
		return ProductBackup::new;
	}

	@Bean
	@StepScope
	public ItemWriter<ProductBackup> writer_230410(
			@Value("#{stepExecutionContext[minId]}") Long minId,
			@Value("#{stepExecutionContext[maxId]}") Long maxId
	) {
		return items -> {
			productBackupRepository.saveAll(items);
		};
	}
}
