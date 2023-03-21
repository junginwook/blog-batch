package blog.study.top.job.review.addPasses;

import blog.study.top.repository.pass.BulkPassEntity;
import blog.study.top.repository.pass.BulkPassRepository;
import blog.study.top.repository.pass.BulkPassStatus;
import blog.study.top.repository.pass.PassEntity;
import blog.study.top.repository.pass.PassRepository;
import blog.study.top.repository.user.UserGroupMappingEntity;
import blog.study.top.repository.user.UserGroupMappingRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AddPassesTasklet implements Tasklet {

	private final BulkPassRepository bulkPassRepository;
	private final UserGroupMappingRepository userGroupMappingRepository;

	private final PassRepository passRepository;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		final LocalDateTime startedAt = LocalDateTime.now().minusDays(1);
		final List<BulkPassEntity> bulkPassEntities = bulkPassRepository.findByStatusAndStartedAtGreaterThan(BulkPassStatus.READY, startedAt);

		int count = 0;
		//대량 이용권 정보를 돌면서 user group에 속한 userId를 조회
		for (BulkPassEntity bulkPassEntity : bulkPassEntities) {
			final List<String> userIds = userGroupMappingRepository.findByUserGroupId(bulkPassEntity.getUserGroupId())
					.stream().map(UserGroupMappingEntity::getUserId).toList();

			count += addPasses(bulkPassEntity, userIds);

			bulkPassEntity.setStatus(BulkPassStatus.COMPLETED);
		}

		return RepeatStatus.FINISHED;
	}

	private int addPasses(BulkPassEntity bulkPassEntity, List<String> userIds) {
		final List<PassEntity> passEntities = new ArrayList<>();
		for (String userId : userIds) {
			PassEntity passEntity = PassEntity.fromBulkPass(bulkPassEntity, userId);
			passEntities.add(passEntity);
		}

		return passRepository.saveAll(passEntities).size();
	}
}
