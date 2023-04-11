package blog.study.top.job.blog.spring_batch_파티셔닝_활용하기;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import blog.study.top.repository.product.repository.ProductBatchRepository;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.ExecutionContext;

@ExtendWith(MockitoExtension.class)
public class ProductIdRangePartitionerTest {
	private static ProductIdRangePartitioner partitioner;

	@Mock
	private ProductBatchRepository productBatchRepository;

	@Test
	public void partitionerTest() {
		//given
		Mockito.lenient()
				.when(productBatchRepository.findMinId(any(LocalDate.class), any(LocalDate.class)))
				.thenReturn(1L);
		Mockito.lenient()
				.when(productBatchRepository.findMaxId(any(LocalDate.class), any(LocalDate.class)))
				.thenReturn(10L);

		partitioner = new ProductIdRangePartitioner(productBatchRepository, LocalDate.of(2021, 1, 20), LocalDate.of(2021, 1, 21));

		//when
		Map<String, ExecutionContext> executionContextMap = partitioner.partition(5);

		//then
		ExecutionContext partition1 = executionContextMap.get("partition0");
		assertThat(partition1.getLong("minId")).isEqualTo(1L);
		assertThat(partition1.getLong("maxId")).isEqualTo(2L);

		ExecutionContext partition5 = executionContextMap.get("partition4");
		assertThat(partition5.getLong("minId")).isEqualTo(9L);
		assertThat(partition5.getLong("maxId")).isEqualTo(10L);
	}
}
