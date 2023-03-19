package blog.study.top.repository.pass;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "bulk_pass")
public class BulkPassEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bulkPassSeq;

	private Integer packageSeq;
	private String userGroupId;

	@Enumerated(EnumType.STRING)
	private BulkPassStatus status;
	private Integer count;

	private LocalDateTime startedAt;
	private LocalDateTime endedAt;
}
