package blog.study.top.repository.pay;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "pay")
@Getter
@NoArgsConstructor
public class PayEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long amount;
	private String txName;
	private LocalDateTime txDateTime;

	private boolean successStatus;

	public PayEntity(Long amount, String txName, LocalDateTime txDateTime) {
		this.amount = amount;
		this.txName = txName;
		this.txDateTime = txDateTime;
	}

	public PayEntity(Long id, Long amount, String txName, LocalDateTime txDateTime) {
		this.id = id;
		this.amount = amount;
		this.txName = txName;
		this.txDateTime = txDateTime;
	}

	public PayEntity(Long amount, boolean successStatus) {
		this.amount = amount;
		this.successStatus = successStatus;
	}

	public void changeName(String txName) {
		this.txName = txName;
	}

	public void success() {
		this.successStatus = true;
	}
}
