package blog.study.top.repository.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class History {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private Long purchaseOrderId;

	@Column
	protected String productIdList;

	@Builder
	public History(Long purchaseOrderId, List<Product> productIdList) {
		this.purchaseOrderId = purchaseOrderId;
		this.productIdList = productIdList.stream()
				.map(product -> String.valueOf(product.getId()))
				.collect(Collectors.joining(", "));
	}
}
