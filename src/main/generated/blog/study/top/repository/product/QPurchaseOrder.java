package blog.study.top.repository.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPurchaseOrder is a Querydsl query type for PurchaseOrder
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPurchaseOrder extends EntityPathBase<PurchaseOrder> {

    private static final long serialVersionUID = -1231176556L;

    public static final QPurchaseOrder purchaseOrder = new QPurchaseOrder("purchaseOrder");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath memo = createString("memo");

    public final ListPath<Product, QProduct> productList = this.<Product, QProduct>createList("productList", Product.class, QProduct.class, PathInits.DIRECT2);

    public QPurchaseOrder(String variable) {
        super(PurchaseOrder.class, forVariable(variable));
    }

    public QPurchaseOrder(Path<? extends PurchaseOrder> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPurchaseOrder(PathMetadata metadata) {
        super(PurchaseOrder.class, metadata);
    }

}

