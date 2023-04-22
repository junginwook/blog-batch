package blog.study.top.repository.pass;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPassEntity is a Querydsl query type for PassEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPassEntity extends EntityPathBase<PassEntity> {

    private static final long serialVersionUID = 1500770967L;

    public static final QPassEntity passEntity = new QPassEntity("passEntity");

    public final blog.study.top.repository.QBaseEntity _super = new blog.study.top.repository.QBaseEntity(this);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> endedAt = createDateTime("endedAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> expiredAt = createDateTime("expiredAt", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final NumberPath<Integer> packageSeq = createNumber("packageSeq", Integer.class);

    public final NumberPath<Integer> passSeq = createNumber("passSeq", Integer.class);

    public final EnumPath<PassStatus> passStatus = createEnum("passStatus", PassStatus.class);

    public final NumberPath<Integer> remainingCount = createNumber("remainingCount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> startedAt = createDateTime("startedAt", java.time.LocalDateTime.class);

    public final StringPath userId = createString("userId");

    public QPassEntity(String variable) {
        super(PassEntity.class, forVariable(variable));
    }

    public QPassEntity(Path<? extends PassEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPassEntity(PathMetadata metadata) {
        super(PassEntity.class, metadata);
    }

}

