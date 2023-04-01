package blog.study.top.job.blog.spring_batch에서_ItemReader를_mock객체로_교체하기;

import blog.study.top.repository.customer.Customer;
import blog.study.top.repository.customer.CustomerBackup;
import jakarta.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class CustomerCopyBatchConfiguration {

	private static final int chunkSize = 10;

	private final JobRepository jobRepository;
	private final EntityManagerFactory entityManagerFactory;
	private final PlatformTransactionManager transactionManager;
	private final JpaPagingItemReader<Customer> reader230401;

	@Bean
	public Job job230401() {
		return new JobBuilder("job230410", jobRepository)
				.start(step230410())
				.build();
	}

	@Bean
	public Step step230410() {
		return new StepBuilder("step230410", jobRepository)
				.<Customer, CustomerBackup>chunk(chunkSize, transactionManager)
				.reader(reader230401)
				.processor(processor())
				.writer(writer())
				.build();
	}

	@Bean
	@StepScope
	public JpaPagingItemReader<Customer> reader230401(
			@Value("#{jobParameters[mailHost]}") String mailHost
	) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("mailHost", "%@" + mailHost);

		return new JpaPagingItemReaderBuilder<Customer>()
				.name("reader230401")
				.pageSize(chunkSize)
				.queryString("SELECT c FROM Customer c WHERE c.email LIKE :mailHost")
				.entityManagerFactory(entityManagerFactory)
				.parameterValues(paramMap)
				.build();
	}

	@Bean
 	public ItemProcessor<Customer, CustomerBackup> processor() {
		return item -> new CustomerBackup(item.getId());
	}

	@Bean
	public JpaItemWriter<CustomerBackup> writer() {
		return new JpaItemWriterBuilder<CustomerBackup>()
				.entityManagerFactory(entityManagerFactory)
				.build();
	}
}
