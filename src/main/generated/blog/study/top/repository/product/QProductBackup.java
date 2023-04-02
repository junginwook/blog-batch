package blog.study.top.repository.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProductBackup is a Querydsl query type for ProductBackup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductBackup extends EntityPathBase<ProductBackup> {

    private static final long serialVersionUID = 646164824L;

    public static final QProductBackup productBackup = new QProductBackup("productBackup");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public QProductBackup(String variable) {
        super(ProductBackup.class, forVariable(variable));
    }

    public QProductBackup(Path<? extends ProductBackup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductBackup(PathMetadata metadata) {
        super(ProductBackup.class, metadata);
    }

}

