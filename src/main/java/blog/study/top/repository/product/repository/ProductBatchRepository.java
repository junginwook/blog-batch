package blog.study.top.repository.product.repository;

import static blog.study.top.repository.product.QProduct.*;

import blog.study.top.repository.product.Product;
import blog.study.top.repository.product.QProduct;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ProductBatchRepository extends QuerydslRepositorySupport {

	private final JPAQueryFactory queryFactory;

	public ProductBatchRepository(JPAQueryFactory queryFactory) {
		super(Product.class);
		this.queryFactory = queryFactory;
	}

	public List<Product> findPageByCreateDate(LocalDate txDate, int pageSize, long offSet) {
		return queryFactory
				.selectFrom(product)
				.where(product.createDate.eq(txDate))
				.limit(pageSize)
				.offset(offSet)
				.fetch();
	}
}
