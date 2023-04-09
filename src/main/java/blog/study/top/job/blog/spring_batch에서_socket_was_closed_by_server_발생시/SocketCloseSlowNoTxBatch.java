package blog.study.top.job.blog.spring_batch에서_socket_was_closed_by_server_발생시;

import blog.study.top.repository.product.Product;
import blog.study.top.repository.product.Store;
import jakarta.persistence.EntityManagerFactory;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SocketCloseSlowNoTxBatch {
	private final JobRepository jobRepository;
	private static final int chunkSize = 10;

	@Bean
	public Job job_230409_3() {
		return new JobBuilder("job_230409_3", jobRepository)
				.start(step_230409_3())
				.build();
	}

	@Bean
	public Step step_230409_3() {
		return new StepBuilder("step_230409_2", jobRepository)
				.<Product, Store>chunk(chunkSize, new ResourcelessTransactionManager())
				.reader(reader_230409_3())
				.processor(processor())
				.writer(writer_230409_3())
				.build();
	}

	@Bean
	public ListItemReader<Product> reader_230409_3() {
		return new ListItemReader<>(List.of(new Product("inwook", 1000L)));
	}

	public ItemProcessor<Product, Store> processor() {
		return item -> {
			log.info("processor start");
			Thread.sleep(150_000);
			log.info("processor end");
			return new Store(item.getName(), item.getName());
		};
	}

	@Bean
	public ItemWriter<Store> writer_230409_3() {
		return items -> log.info("items.size={}", items.size());
	}
}
