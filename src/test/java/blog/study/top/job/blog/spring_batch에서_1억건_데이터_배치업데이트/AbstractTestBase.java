package blog.study.top.job.blog.spring_batch에서_1억건_데이터_배치업데이트;

import blog.study.top.job.blog.TestBatchConfig;
import blog.study.top.job.blog.config.UniqueRunIdIncrementer;
import blog.study.top.repository.pass.PassEntity;
import blog.study.top.repository.pass.PassRepository;
import blog.study.top.repository.pass.PassStatus;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBatchTest
@SpringBootTest(classes = {
		TestBatchConfig.class,
		UniqueRunIdIncrementer.class
})
public class AbstractTestBase {

	@Autowired
	private PassRepository passRepository;

	@BeforeEach
	void setUp() {
		for(int i=0; i< 10000; i++) {
			passRepository.save(
					PassEntity.builder()
							.packageSeq(null)
							.userId("userId")
							.passStatus(PassStatus.PROGRESSED)
							.remainingCount(20)
							.createdAt(LocalDateTime.of(2023, 4, 23, 0, 0, 0))
							.startedAt(LocalDateTime.now())
							.endedAt(LocalDateTime.now())
							.expiredAt(LocalDateTime.now())
							.build()
			);
		}

		passRepository.save(
				PassEntity.builder()
						.packageSeq(null)
						.userId("userId")
						.passStatus(PassStatus.EXPIRED)
						.remainingCount(20)
						.createdAt(LocalDateTime.of(2023, 4, 23, 0, 0, 0))
						.startedAt(LocalDateTime.now())
						.endedAt(LocalDateTime.now())
						.expiredAt(LocalDateTime.now())
						.build()
		);
		passRepository.save(
				PassEntity.builder()
						.packageSeq(null)
						.userId("userId")
						.passStatus(PassStatus.READY)
						.remainingCount(20)
						.createdAt(LocalDateTime.of(2023, 4, 23, 0, 0, 0))
						.startedAt(LocalDateTime.now())
						.endedAt(LocalDateTime.now())
						.expiredAt(LocalDateTime.now())
						.build()
		);
	}
}
