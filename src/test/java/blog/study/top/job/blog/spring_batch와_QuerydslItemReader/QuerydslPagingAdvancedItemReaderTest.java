package blog.study.top.job.blog.spring_batch와_QuerydslItemReader;

import static blog.study.top.repository.product.QProduct.*;
import static org.junit.jupiter.api.Assertions.*;

import blog.study.top.job.blog.TestBatchConfig;
import blog.study.top.repository.pay.PayEntity;
import blog.study.top.repository.product.Product;
import blog.study.top.repository.product.QProduct;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestBatchConfig.class)
class QuerydslPagingAdvancedItemReaderTest {

	private int pageSize = 10;
	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Test
	void testReader() throws Exception {

		QuerydslPagingAdvancedItemReader<Product> reader = new QuerydslPagingAdvancedItemReaderBuilder<Product>()
				.pageSize(pageSize)
				.entityManagerFactory(entityManagerFactory)
				.queryFunction(queryFactory ->
						queryFactory.selectFrom(product)
								.where(product.name.eq("name"))
				)
				.option(new QuerydslPagingAdvancedItemReaderOption(product.id, QuerydslPagingAdvancedItemReaderExpression.ASC))
				.build();
		reader.open(new ExecutionContext());
		reader.afterPropertiesSet();

		reader.read();
	}
}