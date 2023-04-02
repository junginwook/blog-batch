package blog.study.top.job.blog.spring_batch_paging_reader_사용시_같은_조건의_데이터를_읽고_수정할때_문제;

import blog.study.top.repository.pay.PayEntity;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PayPagingPageJobConfiguration {

	private final JobRepository jobRepository;
	private final EntityManagerFactory entityManagerFactory;
	private final PlatformTransactionManager transactionManager;
	private final static int chunkSize = 10;

	@Bean
	public Job job230402_2() {
		return new JobBuilder("job230402_2", jobRepository)
				.start(step230402_2())
				.build();
	}

	@Bean
	@JobScope
	public Step step230402_2() {
		return new StepBuilder("step230402_2", jobRepository)
				.<PayEntity, PayEntity>chunk(chunkSize, transactionManager)
				.reader(reader230402_2())
				.processor(processor230402_2())
				.writer(writer230402_2())
				.build();
	}

	/**
	 * 같은 조건의 데이터를 읽고 수정할 시 update 할때마다 조회 데이터의 숫자가 계속 줄어들기 때문에
	 * page 의 값을 0으로 고정하는 방법이 있다.
	 * 또는 cursor 방식으로 데이터를 읽으면 된다.
	 */
	@Bean
	@StepScope
	public JpaPagingItemReader<PayEntity> reader230402_2() {

		JpaPagingItemReader<PayEntity> reader = new JpaPagingItemReader<>() {
			@Override
			public int getPage() {
				return 0;
			}
		};

		reader.setName("reader230402_2");
		reader.setQueryString("SELECT p FROM pay p WHERE p.successStatus = false");
		reader.setPageSize(chunkSize);
		reader.setEntityManagerFactory(entityManagerFactory);

		return reader;
	}

	@Bean
	@StepScope
	public ItemProcessor<PayEntity, PayEntity> processor230402_2() {
		return item -> {
			item.success();
			return item;
		};
	}

	@Bean
	@StepScope
	public ItemWriter<PayEntity> writer230402_2() {
		return chunk -> {
			for (PayEntity item : chunk) {
				System.out.println("item: " + item.getId());
			}
		};
	}
}
