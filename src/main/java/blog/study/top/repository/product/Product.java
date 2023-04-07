package blog.study.top.repository.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String name;

	@Column
	private long price;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "purchase_order_id")
	private PurchaseOrder purchaseOrder;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Store store;

	@Column
	private long amount;

	@Column
	private long categoryNo;

	@Column
	private LocalDate createDate;

	@Enumerated(EnumType.STRING)
	@Column
	private ProductStatus productStatus;

	@Builder
	public Product(String name, long price, LocalDate createDate) {
		this.name = name;
		this.price = price;
		this.createDate = createDate;
	}

	@Builder
	public Product(String name, long price, long categoryNo, LocalDate createDate) {
		this.name = name;
		this.price = price;
		this.categoryNo = categoryNo;
		this.createDate = createDate;
	}

	@Builder
	public Product(String name, long price) {
		this.name = name;
		this.price = price;
	}

	@Builder
	public Product(int price, LocalDate createDate, ProductStatus status) {
		this.price = price;
		this.createDate = createDate;
		this.productStatus = status;
	}
}
