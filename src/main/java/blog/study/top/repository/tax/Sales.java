package blog.study.top.repository.tax;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Sales {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private long txAmount;

	@Column
	private Long ownerNo;

	public Sales(long txAmount, Long ownerNo) {
		this.txAmount = txAmount;
		this.ownerNo = ownerNo;
	}
}
