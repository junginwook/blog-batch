package blog.study.top.repository.pay;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PayRepository extends JpaRepository<PayEntity, Long> {

	@Query("SELECT p FROM pay p where p.successStatus = true")
	List<PayEntity> findAllSuccess();
}
