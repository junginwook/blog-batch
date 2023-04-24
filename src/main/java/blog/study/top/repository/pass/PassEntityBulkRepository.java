package blog.study.top.repository.pass;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PassEntityBulkRepository {

	static final String TABLE = "pass";

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public void bulkInsert(List<PassDto> passDtoList) {
		var sql = String.format("""
					INSERT INTO `%s` (package_seq, user_id, `pass_status`, remaining_count, created_at, started_at, ended_at, expired_at)
					VALUES (:packageSeq, :userId, :passStatus, :remainingCount, :createdAt, :startedAt, :endedAt, :expiredAt)
				""", TABLE);

		SqlParameterSource[] params = passDtoList
				.stream()
				.map(BeanPropertySqlParameterSource::new)
				.toArray(SqlParameterSource[]::new);

		namedParameterJdbcTemplate.batchUpdate(sql, params);
	}
}
