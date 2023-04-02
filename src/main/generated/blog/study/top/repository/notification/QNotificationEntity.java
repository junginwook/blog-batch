package blog.study.top.repository.notification;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QNotificationEntity is a Querydsl query type for NotificationEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotificationEntity extends EntityPathBase<NotificationEntity> {

    private static final long serialVersionUID = 137952215L;

    public static final QNotificationEntity notificationEntity = new QNotificationEntity("notificationEntity");

    public final EnumPath<NotificationEvent> event = createEnum("event", NotificationEvent.class);

    public final NumberPath<Integer> notificationSeq = createNumber("notificationSeq", Integer.class);

    public final BooleanPath sent = createBoolean("sent");

    public final DateTimePath<java.time.LocalDateTime> sentAt = createDateTime("sentAt", java.time.LocalDateTime.class);

    public final StringPath text = createString("text");

    public final StringPath uuid = createString("uuid");

    public QNotificationEntity(String variable) {
        super(NotificationEntity.class, forVariable(variable));
    }

    public QNotificationEntity(Path<? extends NotificationEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNotificationEntity(PathMetadata metadata) {
        super(NotificationEntity.class, metadata);
    }

}

