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

	public Long findMinId(LocalDate startDate, LocalDate endDate) {
		return queryFactory
				.select(product.id)
				.from(product)
				.where(
						product.createDate.between(startDate, endDate)
				)
				.orderBy(product.id.asc())
				.limit(1)
				.fetchOne();
	}

	public Long findMaxId(LocalDate startDate, LocalDate endDate) {
		return queryFactory
				.select(product.id)
				.from(product)
				.where(
						product.createDate.between(startDate, endDate)
				)
				.orderBy(product.id.desc())
				.limit(1)
				.fetchOne();
	}
}
