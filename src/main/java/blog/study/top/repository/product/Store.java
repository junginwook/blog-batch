package blog.study.top.repository.product;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class Store {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String address;

	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
	private List<Product> products = new ArrayList<>();

	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
	private List<Employee> employees = new ArrayList<>();

	public Store(String name, String address) {
		this.name = name;
		this.address = address;
	}

	public Store(String name) {
		this.name = name;
	}

	public void addProduct(Product product) {
		products.add(product);
	}

	public void addEmployee(Employee employee) {
		employees.add(employee);
	}
}
