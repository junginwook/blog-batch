package blog.study.top.job.blog.spring_batch_파티셔닝_활용하기;

import blog.study.top.repository.product.repository.ProductRepository;
import java.time.LocalDate;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

@Slf4j
@RequiredArgsConstructor
public class ProductIdRangePartitioner implements Partitioner {

	private final ProductRepository productRepository;
	private final LocalDate startDate;
	private final LocalDate endDate;

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		long min = productRepository.findMinId();
	}
}
