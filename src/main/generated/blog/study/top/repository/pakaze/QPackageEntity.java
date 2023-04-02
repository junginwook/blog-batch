package blog.study.top.repository.pakaze;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPackageEntity is a Querydsl query type for PackageEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPackageEntity extends EntityPathBase<PackageEntity> {

    private static final long serialVersionUID = 1178225095L;

    public static final QPackageEntity packageEntity = new QPackageEntity("packageEntity");

    public final blog.study.top.repository.QBaseEntity _super = new blog.study.top.repository.QBaseEntity(this);

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath packageName = createString("packageName");

    public final NumberPath<Integer> packageSeq = createNumber("packageSeq", Integer.class);

    public final NumberPath<Integer> period = createNumber("period", Integer.class);

    public QPackageEntity(String variable) {
        super(PackageEntity.class, forVariable(variable));
    }

    public QPackageEntity(Path<? extends PackageEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPackageEntity(PathMetadata metadata) {
        super(PackageEntity.class, metadata);
    }

}

