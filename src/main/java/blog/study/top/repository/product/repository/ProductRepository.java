package blog.study.top.repository.product.repository;

import blog.study.top.repository.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
