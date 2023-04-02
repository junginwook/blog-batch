package blog.study.top.repository.product.repository;

import blog.study.top.repository.product.StoreHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreHistoryRepository extends JpaRepository<StoreHistory, Long> {

}
