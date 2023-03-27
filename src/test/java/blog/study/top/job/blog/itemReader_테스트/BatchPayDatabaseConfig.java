package blog.study.top.job.blog.itemReader_테스트;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
public class BatchPayDatabaseConfig {
	public static final String CREATE_SQL =
			"CREATE TABLE pay (id BIGINT NOT NULL AUTO_INCREMENT, amount BIGINT, tx_date_time DATETIME, tx_name VARCHAR(10))";

	@Bean
	public DataSource dataSource() {
		EmbeddedDatabaseFactory databaseFactory = new EmbeddedDatabaseFactory();
		databaseFactory.setDatabaseType(H2);
		return databaseFactory.getDatabase();
	}

	@Bean
	public DataSourceInitializer initializer(DataSource dataSource) {
		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		dataSourceInitializer.setDataSource(dataSource);

		Resource create = new ByteArrayResource(CREATE_SQL.getBytes());
		dataSourceInitializer.setDatabasePopulator(new ResourceDatabasePopulator(create));

		return dataSourceInitializer;
	}
}
