package blog.study.top.job.blog.spring_batch_파티셔닝_활용하기;

import blog.study.top.repository.product.repository.ProductBatchRepository;
import blog.study.top.repository.product.repository.ProductRepository;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

@Slf4j
@RequiredArgsConstructor
public class ProductIdRangePartitioner implements Partitioner {

	private final ProductBatchRepository productBatchRepository;
	private final LocalDate startDate;
	private final LocalDate endDate;

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		long min = productBatchRepository.findMinId(startDate, endDate);
		long max = productBatchRepository.findMaxId(startDate, endDate);
		log.info("min={}", min);
		log.info("max={}", max);
		long targetSize = (max - min) / gridSize + 1;

		Map<String, ExecutionContext> result = new HashMap<>();
		long number = 0;
		long start = min;
		long end = start + targetSize - 1;

		while (start <= max) {
			ExecutionContext value = new ExecutionContext();
			result.put("partition" + number, value);

			if (end >= max) {
				end = max;
			}

			value.putLong("minId", start);
			value.putLong("maxId", end);
			start += targetSize;
			end += targetSize;
			number++;
		}

		return result;
	}
}
