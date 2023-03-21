package blog.study.top.repository.pass;

import blog.study.top.repository.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "pass")
@NoArgsConstructor
public class PassEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer passSeq;

	private Integer packageSeq;

	private String userId;

	@Enumerated(EnumType.STRING)
	private PassStatus status;

	private Integer remainingCount;

	private LocalDateTime startedAt;

	private LocalDateTime endedAt;

	private LocalDateTime expiredAt;

	@Builder
	public PassEntity(Integer packageSeq, String userId, PassStatus status, Integer remainingCount, LocalDateTime startedAt,
			LocalDateTime endedAt, LocalDateTime expiredAt) {
		this.packageSeq = packageSeq;
		this.userId = userId;
		this.status = status;
		this.remainingCount = remainingCount;
		this.startedAt = startedAt;
		this.endedAt = endedAt;
		this.expiredAt = expiredAt;
	}

	public static PassEntity fromBulkPass(BulkPassEntity bulkPassEntity, String userId) {

		return PassEntity.builder()
				.packageSeq(bulkPassEntity.getPackageSeq())
				.userId(userId)
				.status(PassStatus.PROGRESSED)
				.remainingCount(bulkPassEntity.getCount())
				.startedAt(LocalDateTime.now())
				.endedAt(LocalDateTime.now())
				.expiredAt(LocalDateTime.now())
				.build();
	}
}
