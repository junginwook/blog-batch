package blog.study.top.job.blog.spring_batch와_QuerydslItemReader.expression;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;

public interface WhereStringFunction {

	BooleanExpression apply(StringPath id, int page, String currentId);
}
