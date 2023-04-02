package blog.study.top.job.blog.spring_batch와_QuerydslItemReader;

import static blog.study.top.repository.product.QProduct.*;

import blog.study.top.repository.product.Product;
import blog.study.top.repository.product.ProductBackup;
import blog.study.top.repository.product.QProduct;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class QuerydslPagingItemReaderConfiguration {

	public static final String JOB_NAME = "querydslPagingReaderJob";
	private final EntityManagerFactory entityManagerFactory;
	private final PlatformTransactionManager transactionManager;
	private final JobRepository jobRepository;
	private final static int chunkSize = 10;

	@Bean
	public Job job230402_4() {
		return new JobBuilder("job230402_4", jobRepository)
				.start(step230402_4())
				.build();
	}

	@Bean
	public Step step230402_4() {
		return new StepBuilder("step230402_4", jobRepository)
				.<Product, ProductBackup>chunk(chunkSize, transactionManager)
				.reader(reader230402_4(null))
				.processor(processor())
				.writer(writer230402_4())
				.build();
	}

	@Bean
	@StepScope
	public QuerydslPagingItemReader<Product> reader230402_4(
			@Value("#{jobParameters[txDate]}") String localDate
	) {
		return new QuerydslPagingItemReader<Product>(entityManagerFactory, chunkSize, queryFactory -> queryFactory
				.selectFrom(product)
				.where(product.createDate.eq(LocalDate.parse(localDate))));
	}

	private ItemProcessor<Product, ProductBackup> processor() {
		return item -> new ProductBackup(item.getName(), item.getPrice());
	}

	@Bean
	public JpaItemWriter<ProductBackup> writer230402_4() {
		return new JpaItemWriterBuilder<ProductBackup>()
				.entityManagerFactory(entityManagerFactory)
				.build();
	}
}
