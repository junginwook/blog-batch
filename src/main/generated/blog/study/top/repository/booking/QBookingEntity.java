package blog.study.top.repository.booking;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookingEntity is a Querydsl query type for BookingEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookingEntity extends EntityPathBase<BookingEntity> {

    private static final long serialVersionUID = -573661875L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookingEntity bookingEntity = new QBookingEntity("bookingEntity");

    public final blog.study.top.repository.QBaseEntity _super = new blog.study.top.repository.QBaseEntity(this);

    public final BooleanPath attended = createBoolean("attended");

    public final NumberPath<Integer> bookingSeq = createNumber("bookingSeq", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> cancelledAt = createDateTime("cancelledAt", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> endedAt = createDateTime("endedAt", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final blog.study.top.repository.pass.QPassEntity passEntity;

    public final NumberPath<Integer> passSeq = createNumber("passSeq", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> startedAt = createDateTime("startedAt", java.time.LocalDateTime.class);

    public final EnumPath<BookingStatus> status = createEnum("status", BookingStatus.class);

    public final BooleanPath usedPass = createBoolean("usedPass");

    public final blog.study.top.repository.user.QUserEntity userEntity;

    public final StringPath userId = createString("userId");

    public QBookingEntity(String variable) {
        this(BookingEntity.class, forVariable(variable), INITS);
    }

    public QBookingEntity(Path<? extends BookingEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookingEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookingEntity(PathMetadata metadata, PathInits inits) {
        this(BookingEntity.class, metadata, inits);
    }

    public QBookingEntity(Class<? extends BookingEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.passEntity = inits.isInitialized("passEntity") ? new blog.study.top.repository.pass.QPassEntity(forProperty("passEntity")) : null;
        this.userEntity = inits.isInitialized("userEntity") ? new blog.study.top.repository.user.QUserEntity(forProperty("userEntity")) : null;
    }

}

