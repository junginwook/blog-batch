package blog.study.top.job.blog.spring_batch에서_jpa_엔플러스일_문제;

import blog.study.top.repository.product.Store;
import blog.study.top.repository.product.StoreHistory;
import jakarta.persistence.EntityManagerFactory;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class StoreBackupBatchConfiguration {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final EntityManagerFactory entityManagerFactory;

	private final static int chunkSize = 10;

	@Bean
	public Job job230402_3() {
		return new JobBuilder("job230402_3", jobRepository)
				.start(step230402_3())
				.build();
	}

	@Bean
	public Step step230402_3() {
		return new StepBuilder("step230402_3", jobRepository)
				.<Store, StoreHistory>chunk(chunkSize, transactionManager)
				.reader(reader230402_3(null))
				.processor(processor())
				.writer(writer230402_3())
				.build();
	}

	@Bean
	@StepScope
	public JpaCursorItemReader<Store> reader230402_3(
			@Value("#{jobParameters[address]}") String address
	) {
		Map<String, Object> parameters = new LinkedHashMap<>();
		parameters.put("address", address + "%");

		return new JpaCursorItemReaderBuilder<Store>()
				.name("reader230402_3")
				.queryString("SELECT s FROM Store s WHERE s.address LIKE :address")
				.parameterValues(parameters)
				.entityManagerFactory(entityManagerFactory)
				.build();
	}

	public ItemProcessor<Store, StoreHistory> processor() {
		return item -> new StoreHistory(item, item.getProducts(), item.getEmployees());
	}

	@Bean
	public JpaItemWriter<StoreHistory> writer230402_3() {
		return new JpaItemWriterBuilder<StoreHistory>()
				.entityManagerFactory(entityManagerFactory)
				.build();
	}
}
