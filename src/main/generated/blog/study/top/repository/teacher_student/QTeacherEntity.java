package blog.study.top.repository.teacher_student;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTeacherEntity is a Querydsl query type for TeacherEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTeacherEntity extends EntityPathBase<TeacherEntity> {

    private static final long serialVersionUID = -1555730981L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTeacherEntity teacherEntity = new QTeacherEntity("teacherEntity");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final blog.study.top.repository.product.QProduct product;

    public final ListPath<StudentEntity, QStudentEntity> students = this.<StudentEntity, QStudentEntity>createList("students", StudentEntity.class, QStudentEntity.class, PathInits.DIRECT2);

    public QTeacherEntity(String variable) {
        this(TeacherEntity.class, forVariable(variable), INITS);
    }

    public QTeacherEntity(Path<? extends TeacherEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTeacherEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTeacherEntity(PathMetadata metadata, PathInits inits) {
        this(TeacherEntity.class, metadata, inits);
    }

    public QTeacherEntity(Class<? extends TeacherEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new blog.study.top.repository.product.QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

