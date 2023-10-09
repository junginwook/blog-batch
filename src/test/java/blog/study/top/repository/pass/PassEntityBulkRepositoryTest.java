package blog.study.top.repository.pass;

import blog.study.top.job.blog.TestBatchConfig;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
		classes = {
				TestBatchConfig.class,
		PassEntityBulkRepository.class}
)
class PassEntityBulkRepositoryTest {

	@Autowired
	private PassEntityBulkRepository passEntityBulkRepository;

	@Test
	void testBatchInsertPassEntity() {
		var easyRandom = PassEntityFixtureFactory.get(
				LocalDate.of(2023, 4, 20),
				LocalDate.of(2023, 7, 27)
		);
		for(int i =0 ; i< 10; i++) {
			List<PassDto> passDtoList = IntStream.range(0, 100000)
					.parallel()
					.mapToObj(item -> easyRandom.nextObject(PassDto.class))
					.toList();

			passEntityBulkRepository.bulkInsert(passDtoList);
		}
	}
}