package blog.study.top.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Pay {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long amount;
	private String txName;
	private LocalDateTime txDateTime;

	public Pay(Long amount, String txName, LocalDateTime txDateTime) {
		this.amount = amount;
		this.txName = txName;
		this.txDateTime = txDateTime;
	}

	public Pay(Long id, Long amount, String txName, LocalDateTime txDateTime) {
		this.id = id;
		this.amount = amount;
		this.txName = txName;
		this.txDateTime = txDateTime;
	}
}
