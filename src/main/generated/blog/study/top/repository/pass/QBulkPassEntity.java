package blog.study.top.repository.pass;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBulkPassEntity is a Querydsl query type for BulkPassEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBulkPassEntity extends EntityPathBase<BulkPassEntity> {

    private static final long serialVersionUID = 2110265289L;

    public static final QBulkPassEntity bulkPassEntity = new QBulkPassEntity("bulkPassEntity");

    public final NumberPath<Integer> bulkPassSeq = createNumber("bulkPassSeq", Integer.class);

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> endedAt = createDateTime("endedAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> packageSeq = createNumber("packageSeq", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> startedAt = createDateTime("startedAt", java.time.LocalDateTime.class);

    public final EnumPath<BulkPassStatus> status = createEnum("status", BulkPassStatus.class);

    public final StringPath userGroupId = createString("userGroupId");

    public QBulkPassEntity(String variable) {
        super(BulkPassEntity.class, forVariable(variable));
    }

    public QBulkPassEntity(Path<? extends BulkPassEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBulkPassEntity(PathMetadata metadata) {
        super(BulkPassEntity.class, metadata);
    }

}

