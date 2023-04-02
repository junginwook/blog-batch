package blog.study.top.job.blog.spring_batch와_QuerydslItemReader;

import blog.study.top.repository.product.Product;
import blog.study.top.repository.product.repository.ProductBatchRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.database.AbstractPagingItemReader;

public class ProductRepositoryItemReader extends AbstractPagingItemReader<Product> {

	private final ProductBatchRepository productBatchRepository;
	private final LocalDate txDate;

	public ProductRepositoryItemReader(ProductBatchRepository productBatchRepository, LocalDate txDate, int pageSize) {
		this.productBatchRepository = productBatchRepository;
		this.txDate = txDate;
		setPageSize(pageSize);
	}

	@Override
	protected void doReadPage() {
		if (results == null) {
			results = new ArrayList<>();
		} else {
			results.clear();
		}

		List<Product> products = productBatchRepository.findPageByCreateDate(txDate, getPageSize(), getPage());

		results.addAll(products);
	}
}
