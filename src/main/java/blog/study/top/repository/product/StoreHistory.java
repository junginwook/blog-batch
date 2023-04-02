package blog.study.top.repository.product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class StoreHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String storeName;
	private String productNames;
	private String employeeNames;

	public StoreHistory(Store store, List<Product> products, List<Employee> employees) {
		this.storeName = store.getName();
		this.productNames = products.stream()
				.map(Product::getName)
				.collect(Collectors.joining(","));
		this.employeeNames = employees.stream()
				.map(Employee::getName)
				.collect(Collectors.joining(","));
	}
}
