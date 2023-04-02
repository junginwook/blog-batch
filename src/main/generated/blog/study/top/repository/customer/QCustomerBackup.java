package blog.study.top.repository.customer;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCustomerBackup is a Querydsl query type for CustomerBackup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCustomerBackup extends EntityPathBase<CustomerBackup> {

    private static final long serialVersionUID = 249833110L;

    public static final QCustomerBackup customerBackup = new QCustomerBackup("customerBackup");

    public final NumberPath<Long> customerId = createNumber("customerId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QCustomerBackup(String variable) {
        super(CustomerBackup.class, forVariable(variable));
    }

    public QCustomerBackup(Path<? extends CustomerBackup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCustomerBackup(PathMetadata metadata) {
        super(CustomerBackup.class, metadata);
    }

}

