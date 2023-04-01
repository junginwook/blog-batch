package blog.study.top.job.blog.spring_batch에서_영속성_컨텍스트_문제;

import static org.assertj.core.api.Java6Assertions.assertThat;

import blog.study.top.job.blog.TestBatchConfig;
import blog.study.top.repository.product.Product;
import blog.study.top.repository.product.PurchaseOrder;
import blog.study.top.repository.product.repository.PurchaseOrderRepository;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBatchTest
@SpringBootTest(classes = {
		EntityContextConfiguration.class,
		TestBatchConfig.class}
)
class EntityContextConfigurationTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;

	@BeforeEach
	public void setup() {
		for(int i = 0; i < 100; i++) {
			purchaseOrderRepository.save(
					PurchaseOrder.builder()
							.memo("도착하면 역락주세요")
							.productList(
									Arrays.asList(
											Product.builder().name("마우스").amount(10000L).build(),
											Product.builder().name("키보드").amount(30000L).build()
									)
							)
							.build()
			);
		}
	}

	@Test
	void testJob() throws Exception {
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();

		assertThat(purchaseOrderRepository.findAll().size()).isEqualTo(100);
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
	}
}