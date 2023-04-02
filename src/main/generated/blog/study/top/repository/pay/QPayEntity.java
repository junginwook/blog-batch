package blog.study.top.repository.pay;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPayEntity is a Querydsl query type for PayEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPayEntity extends EntityPathBase<PayEntity> {

    private static final long serialVersionUID = 1851958123L;

    public static final QPayEntity payEntity = new QPayEntity("payEntity");

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath successStatus = createBoolean("successStatus");

    public final DateTimePath<java.time.LocalDateTime> txDateTime = createDateTime("txDateTime", java.time.LocalDateTime.class);

    public final StringPath txName = createString("txName");

    public QPayEntity(String variable) {
        super(PayEntity.class, forVariable(variable));
    }

    public QPayEntity(Path<? extends PayEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPayEntity(PathMetadata metadata) {
        super(PayEntity.class, metadata);
    }

}

