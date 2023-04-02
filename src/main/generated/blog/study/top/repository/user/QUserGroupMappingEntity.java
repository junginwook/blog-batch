package blog.study.top.repository.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserGroupMappingEntity is a Querydsl query type for UserGroupMappingEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserGroupMappingEntity extends EntityPathBase<UserGroupMappingEntity> {

    private static final long serialVersionUID = -1633129722L;

    public static final QUserGroupMappingEntity userGroupMappingEntity = new QUserGroupMappingEntity("userGroupMappingEntity");

    public final blog.study.top.repository.QBaseEntity _super = new blog.study.top.repository.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath userGroupId = createString("userGroupId");

    public final StringPath userGroupName = createString("userGroupName");

    public final StringPath userId = createString("userId");

    public QUserGroupMappingEntity(String variable) {
        super(UserGroupMappingEntity.class, forVariable(variable));
    }

    public QUserGroupMappingEntity(Path<? extends UserGroupMappingEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserGroupMappingEntity(PathMetadata metadata) {
        super(UserGroupMappingEntity.class, metadata);
    }

}

