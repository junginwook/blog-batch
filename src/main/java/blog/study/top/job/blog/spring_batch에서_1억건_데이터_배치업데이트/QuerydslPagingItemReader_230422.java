package blog.study.top.job.blog.spring_batch에서_1억건_데이터_배치업데이트;


import static blog.study.top.repository.pass.QPassEntity.*;

import blog.study.top.job.blog.spring_batch와_QuerydslItemReader.QuerydslPagingAdvancedItemReader;
import blog.study.top.job.blog.spring_batch와_QuerydslItemReader.QuerydslPagingAdvancedItemReaderBuilder;
import blog.study.top.job.blog.spring_batch와_QuerydslItemReader.QuerydslPagingAdvancedItemReaderExpression;
import blog.study.top.job.blog.spring_batch와_QuerydslItemReader.QuerydslPagingAdvancedItemReaderOption;
import blog.study.top.repository.pass.PassEntity;
import blog.study.top.repository.pass.PassStatus;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
@Slf4j
@RequiredArgsConstructor
@Configuration
public class QuerydslPagingItemReader_230422 {

	private final EntityManagerFactory entityManagerFactory;
	private final PlatformTransactionManager transactionManager;
	private final JobRepository jobRepository;
	private final DataSource dataSource;
	private final static int chunkSize = 2000;

	@Bean
	public Job job_230422() {
		return new JobBuilder("job_230422", jobRepository)
				.start(step_230422())
				.build();
	}

	@Bean
	public Step step_230422() {
		return new StepBuilder("step_230422", jobRepository)
				.<PassEntity, Map<String, Object>>chunk(chunkSize, transactionManager)
				.reader(reader_230422(null))
				.processor(itemProcessor_230422())
				.writer(itemWriter_230422())
				.build();
	}

	@Bean
	@StepScope
	public QuerydslPagingAdvancedItemReader<PassEntity> reader_230422(
			@Value("#{jobParameters[createdAt]}") LocalDate createdAt
	) {
		return new QuerydslPagingAdvancedItemReaderBuilder<PassEntity>()
				.pageSize(chunkSize)
				.entityManagerFactory(entityManagerFactory)
				.queryFunction(queryFactory ->
						queryFactory.selectFrom(passEntity)
								.where(
										passEntity.createdAt.eq(createdAt.atTime(0, 0, 0)),
										passEntity.passStatus.eq(PassStatus.PROGRESSED)
								)
				)
				.option(new QuerydslPagingAdvancedItemReaderOption(passEntity.passSeq, QuerydslPagingAdvancedItemReaderExpression.ASC))
				.build();
	}

	public ItemProcessor<PassEntity, Map<String, Object>> itemProcessor_230422() {

		return item -> {
			Map<String, Object> map = new HashMap();
			map.put("passStatus", PassStatus.EXPIRED.name());
			map.put("passSeq", item.getPassSeq());
			return map;
		};
	}

	@Bean
	public JdbcBatchItemWriter<Map<String, Object>> itemWriter_230422() {
		return new JdbcBatchItemWriterBuilder<Map<String, Object>>()
				.dataSource(dataSource)
				.sql("update pass set pass_status = :passStatus where pass_seq = :passSeq")
				.build();
	}
}
