package blog.study.top.job.blog.spring_batch_fetch_join시_메모리에서_페이징_처리_사전_차단하기;

import blog.study.top.repository.teacher_student.TeacherEntity;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class BatchJobConfiguration_20231008 {
	private static final String JOB_NAME = "job";
	private static final int chunkSize = 10;
	public final EntityManagerFactory entityManagerFactory;
	public final PlatformTransactionManager transactionManager;
	public final JobRepository jobRepository;
	public final DataSource dataSource;

	@Bean
	public Job job_231008() {
		return new JobBuilder(JOB_NAME, jobRepository)
				.start(step_231008())
				.build();
	}

	@Bean
 	public Step step_231008() {
		return new StepBuilder(JOB_NAME + "_231008", jobRepository)
				.<TeacherEntity, TeacherEntity>chunk(chunkSize, transactionManager)
				.reader(reader())
				.writer(writer())
				.build();
	}

	@Bean(name = JOB_NAME)
	@StepScope
	public JpaPagingItemReader<TeacherEntity> reader() {
		return new JpaPagingItemReaderBuilder<TeacherEntity>()
				.name(JOB_NAME + "_reader")
				.entityManagerFactory(entityManagerFactory)
				.queryString("SELECT distinct(t) FROM TeacherEntity t JOIN FETCH t.students")
				.pageSize(chunkSize)
				.build();
	}

	@Bean
	public JpaItemWriter<TeacherEntity> writer() {
		return new JpaItemWriterBuilder<TeacherEntity>()
				.entityManagerFactory(entityManagerFactory)
				.build();
	}
}
