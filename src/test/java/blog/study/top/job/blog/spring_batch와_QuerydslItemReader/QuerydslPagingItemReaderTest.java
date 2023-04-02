package blog.study.top.job.blog.spring_batch와_QuerydslItemReader;

import static blog.study.top.repository.product.QProduct.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import blog.study.top.job.blog.TestBatchConfig;
import blog.study.top.job.blog.config.QuerydslConfiguration;
import blog.study.top.repository.product.Product;
import blog.study.top.repository.product.QProduct;
import blog.study.top.repository.product.repository.ProductRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBatchTest
@SpringBootTest(classes = {
		QuerydslConfiguration.class,
		TestBatchConfig.class}
)
class QuerydslPagingItemReaderTest {

	@Autowired
	private EntityManagerFactory entityManagerFactory;
	@Autowired
	private ProductRepository productRepository;

	@Test
	void testReader() throws Exception {
		LocalDate txDate = LocalDate.of(2020, 10, 12);
		String name = "a";
		int expected1 = 1000;
		int expected2 = 2000;
		productRepository.save(new Product(name, expected1, txDate));
		productRepository.save(new Product(name, expected2, txDate));

		int pageSize = 1;

		QuerydslPagingItemReader<Product> reader = new QuerydslPagingItemReader<>(entityManagerFactory, pageSize,
				queryFactory -> queryFactory.selectFrom(product)
						.where(product.createDate.eq(txDate)));
		reader.open(new ExecutionContext());

		//when
		Product read = reader.read();
		Product read2 = reader.read();
		Product read3 = reader.read();

		assertThat(read.getPrice()).isEqualTo(expected1);
		assertThat(read2.getPrice()).isEqualTo(expected2);
		assertThat(read3).isNull();
	}
}