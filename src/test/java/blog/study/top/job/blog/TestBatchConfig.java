package blog.study.top.job.blog;

import blog.study.top.job.blog.config.QuerydslConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@TestConfiguration
@Import(QuerydslConfiguration.class)
@EnableAutoConfiguration
@EntityScan("blog.study.top.repository")
@EnableJpaRepositories("blog.study.top.repository")
public class TestBatchConfig {
}
