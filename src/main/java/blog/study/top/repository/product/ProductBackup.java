package blog.study.top.repository.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ProductBackup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String name;

	@Column
	private long price;

	public ProductBackup(String name, long price) {
		this.name = name;
		this.price = price;
	}

	public ProductBackup(Product product) {
		this.name = product.getName();
		this.price = product.getPrice();
	}
}
