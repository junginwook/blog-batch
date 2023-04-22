package blog.study.top.repository.pass;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class PassDto {

	private Integer passSeq;

	private Integer packageSeq;

	private String userId;

	private String passStatus;

	private Integer remainingCount;

	private LocalDate startedAt;

	private LocalDate endedAt;

	private LocalDate expiredAt;

	private LocalDate createdAt;

	public PassDto(Integer packageSeq, String userId, String passStatus, Integer remainingCount, LocalDate startedAt,
			LocalDate endedAt, LocalDate expiredAt, LocalDate createdAt) {
		this.packageSeq = packageSeq;
		this.userId = userId;
		this.passStatus = passStatus;
		this.remainingCount = remainingCount;
		this.startedAt = startedAt;
		this.endedAt = endedAt;
		this.expiredAt = expiredAt;
		this.createdAt = createdAt;
	}
}
