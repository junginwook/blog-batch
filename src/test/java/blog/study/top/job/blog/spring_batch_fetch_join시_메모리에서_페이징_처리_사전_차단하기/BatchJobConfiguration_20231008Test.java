package blog.study.top.job.blog.spring_batch_fetch_join시_메모리에서_페이징_처리_사전_차단하기;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import blog.study.top.job.blog.TestBatchConfig;
import blog.study.top.job.blog.spring_batch_jpaCursorItemReader도입.JpaCursorItemReaderJobConfig;
import blog.study.top.repository.teacher_student.StudentEntity;
import blog.study.top.repository.teacher_student.TeacherEntity;
import blog.study.top.repository.teacher_student.TeacherRepository;

import org.junit.jupiter.api.Test;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBatchTest
@SpringBootTest(classes = {BatchJobConfiguration_20231008.class, TestBatchConfig.class,})
class BatchJobConfiguration_20231008Test {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;
	@Autowired
	private TeacherRepository teacherRepository;

	@Test
	void test_jpa_paging() throws Exception {
		//given
		for (int i = 0; i < 10; i++) {
			String teacherName = i + "선생님";
			TeacherEntity teacher = new TeacherEntity(teacherName);
			teacher.addStudent(new StudentEntity(teacherName + "의 학생1"));
			teacher.addStudent(new StudentEntity(teacherName + "의 학생2"));
			teacherRepository.save(teacher);
		}

		JobParameters jobParameters = jobLauncherTestUtils.getUniqueJobParametersBuilder()
				.toJobParameters();

		//when
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

		//then
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
	}
}