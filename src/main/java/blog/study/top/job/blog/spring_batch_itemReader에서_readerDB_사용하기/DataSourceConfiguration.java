package blog.study.top.job.blog.spring_batch_itemReader에서_readerDB_사용하기;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@RequiredArgsConstructor
@Configuration
public class DataSourceConfiguration {
	private static final String PROPERTIES = "spring.datasource.hikari";
	public static final String MASTER_DATASOURCE = "dataSource";
	public static final String READER_DATASOURCE = "readerDataSource";

	@Bean(MASTER_DATASOURCE)
	@Primary
	@ConfigurationProperties(PROPERTIES)
	public DataSource dataSource() {
		HikariDataSource hikariDataSource = DataSourceBuilder.create()
				.type(HikariDataSource.class)
				.build();
		return hikariDataSource;
	}

	@Bean(READER_DATASOURCE)
	@ConfigurationProperties(PROPERTIES)
	public DataSource readerDataSource() {
		HikariDataSource hikariDataSource =
				DataSourceBuilder
						.create()
				.type(HikariDataSource.class)
				.build();
		hikariDataSource.setReadOnly(true);
		return hikariDataSource;
	}
}
