package blog.study.top.repository.tax;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Tax {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private Long amount;

	@Column
	private Long ownerNo;

	@Builder
	public Tax(Long amount, Long ownerNo) {
		this.amount = amount;
		this.ownerNo = ownerNo;
	}
}
