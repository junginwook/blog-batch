package blog.study.top.job.blog.spring_batch와_QuerydslItemReader;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;

public enum QuerydslPagingAdvancedItemReaderExpression {
	ASC,
	DESC;

	public boolean isAsc() {
		return this == ASC;
	}

	public <N extends Number & Comparable<?>> OrderSpecifier<N> order(NumberPath<N> id) {
		return isAsc() ? id.asc() : id.desc();
	}

	public <N extends Number & Comparable<?>> BooleanExpression where(NumberPath<N> id, N currentId) {
		return isAsc() ? id.gt(currentId) : id.lt(currentId);
	}
}
