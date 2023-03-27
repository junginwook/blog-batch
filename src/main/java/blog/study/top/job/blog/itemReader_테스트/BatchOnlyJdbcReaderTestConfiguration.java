package blog.study.top.job.blog.itemReader_테스트;

import blog.study.top.repository.pay.PayEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchOnlyJdbcReaderTestConfiguration {
	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static final String JOB_NAME = "batchOnlyJdbcReaderTestJob";

	private final DataSource dataSource;

	private int CHUNK_SIZE;

	@Value("${spring.batch.chunk-size:1000}")
	public void setCHUNK_SIZE(int chunk_size) {
		this.CHUNK_SIZE = chunk_size;
	}

	@Bean
	@StepScope
	public JdbcPagingItemReader<PayEntity> batchOnlyJdbcReaderTestJobReader(
			@Value("#{jobParameters[txName]}") String txName
	) throws Exception {

		Map<String, Object> params = new HashMap<>();
		params.put("txName", txName);

		SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
		queryProvider.setDataSource(dataSource);
		queryProvider.setSelectClause("id, amount, tx_name, tx_date_time");
		queryProvider.setFromClause("from pay");
		queryProvider.setWhereClause("where tx_name = :txName");
		queryProvider.setSortKey("id");

		return new JdbcPagingItemReaderBuilder<PayEntity>()
				.name("batchOnlyJdbcReaderTestJobReader")
				.pageSize(CHUNK_SIZE)
				.fetchSize(CHUNK_SIZE)
				.dataSource(dataSource)
				.rowMapper(getRowMapper())
				.queryProvider(queryProvider.getObject())
				.parameterValues(params)
				.build();
	}

	private RowMapper<PayEntity> getRowMapper() {
		return (rs, rowNum) -> new PayEntity(
				rs.getLong("amount"),
				rs.getString("tx_name"),
				rs.getObject("tx_date_time", LocalDateTime.class)
		);
	}
}
