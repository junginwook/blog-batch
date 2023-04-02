package blog.study.top.repository.tax;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTax is a Querydsl query type for Tax
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTax extends EntityPathBase<Tax> {

    private static final long serialVersionUID = -1316526610L;

    public static final QTax tax = new QTax("tax");

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> ownerNo = createNumber("ownerNo", Long.class);

    public QTax(String variable) {
        super(Tax.class, forVariable(variable));
    }

    public QTax(Path<? extends Tax> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTax(PathMetadata metadata) {
        super(Tax.class, metadata);
    }

}

