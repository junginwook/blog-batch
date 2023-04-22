package blog.study.top.repository.pass;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PassRepository extends JpaRepository<PassEntity, Integer> {

	List<PassEntity> findPassEntitiesByPassStatus(PassStatus passStatus);
	@Transactional
	@Modifying
	@Query(value = "UPDATE PassEntity p "
			+ "SET p.remainingCount = :remainingCount, "
			+ "p.modifiedAt = CURRENT_TIMESTAMP "
			+ "WHERE p.passSeq = :passSeq")
	int updateRemainingCount(Integer passSeq, Integer remainingCount);
}
