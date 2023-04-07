package blog.study.top.job.blog.spring_batch와_QuerydslItemReader;

import static blog.study.top.repository.product.QProduct.*;
import static org.assertj.core.api.Assertions.assertThat;

import blog.study.top.job.blog.TestBatchConfig;
import blog.study.top.repository.product.Product;
import blog.study.top.repository.product.repository.ProductRepository;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBatchTest
@SpringBootTest(classes = {TestBatchConfig.class,
		QuerydslPagingItemReaderConfiguration.class,
})
class QuerydslPagingAdvancedItemReaderTest {

	private int pageSize = 9;
	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Autowired
	private ProductRepository productRepository;

	@BeforeEach
	void setUp() {
		for(int i = 1; i <= 10; i++) {
			productRepository.save(new Product("name" + i, 1000, LocalDate.now()));
		}
	}

	@Test
	void testReader_ASC() throws Exception {

		QuerydslPagingAdvancedItemReader<Product> reader = new QuerydslPagingAdvancedItemReaderBuilder<Product>()
				.pageSize(pageSize)
				.entityManagerFactory(entityManagerFactory)
				.queryFunction(queryFactory ->
						queryFactory.selectFrom(product)
								.where(product.price.eq(1000L))
				)
				.option(new QuerydslPagingAdvancedItemReaderOption(product.id, QuerydslPagingAdvancedItemReaderExpression.ASC))
				.build();
		reader.open(new ExecutionContext());
		reader.afterPropertiesSet();

		assertThat(reader.read().getName()).isEqualTo("name1");
		assertThat(reader.read().getName()).isEqualTo("name2");
		assertThat(reader.read().getName()).isEqualTo("name3");
		assertThat(reader.read().getName()).isEqualTo("name4");
		assertThat(reader.read().getName()).isEqualTo("name5");
		assertThat(reader.read().getName()).isEqualTo("name6");
		assertThat(reader.read().getName()).isEqualTo("name7");
		assertThat(reader.read().getName()).isEqualTo("name8");
		assertThat(reader.read().getName()).isEqualTo("name9");
		assertThat(reader.read().getName()).isEqualTo("name10");
		assertThat(reader.read()).isNull();
	}

	@Test
	void testReader_DESC() throws Exception {

		QuerydslPagingAdvancedItemReader<Product> reader = new QuerydslPagingAdvancedItemReaderBuilder<Product>()
				.pageSize(pageSize)
				.entityManagerFactory(entityManagerFactory)
				.queryFunction(queryFactory ->
						queryFactory.selectFrom(product)
								.where(product.price.eq(1000L))
				)
				.option(new QuerydslPagingAdvancedItemReaderOption(product.id, QuerydslPagingAdvancedItemReaderExpression.DESC))
				.build();
		reader.open(new ExecutionContext());
		reader.afterPropertiesSet();

		assertThat(reader.read().getName()).isEqualTo("name10");
		assertThat(reader.read().getName()).isEqualTo("name9");
		assertThat(reader.read().getName()).isEqualTo("name8");
		assertThat(reader.read().getName()).isEqualTo("name7");
		assertThat(reader.read().getName()).isEqualTo("name6");
		assertThat(reader.read().getName()).isEqualTo("name5");
		assertThat(reader.read().getName()).isEqualTo("name4");
		assertThat(reader.read().getName()).isEqualTo("name3");
		assertThat(reader.read().getName()).isEqualTo("name2");
		assertThat(reader.read().getName()).isEqualTo("name1");
		assertThat(reader.read()).isNull();
	}
}