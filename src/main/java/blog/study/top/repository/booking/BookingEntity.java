package blog.study.top.repository.booking;

import blog.study.top.repository.BaseEntity;
import blog.study.top.repository.pass.PassEntity;
import blog.study.top.repository.user.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "booking")
public class BookingEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bookingSeq;
	private Integer passSeq;
	private String userId;

	@Enumerated(EnumType.STRING)
	private BookingStatus status;
	private boolean usedPass;
	private boolean attended;

	private LocalDateTime startedAt;
	private LocalDateTime endedAt;
	private LocalDateTime cancelledAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", insertable = false, updatable = false)
	private UserEntity userEntity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "passSeq", insertable = false, updatable = false)
	private PassEntity passEntity;

	public LocalDateTime getStatisticsAt() {
		return this.endedAt.withHour(0).withMinute(0).withSecond(0).withNano(0);
	}

}
