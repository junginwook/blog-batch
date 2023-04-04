package blog.study.top.job.blog.spring_batch와_QuerydslItemReader;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import java.lang.reflect.Field;

public class QuerydslPagingAdvancedItemReaderOption<T, N extends Number & Comparable<?>> {

	NumberPath<N> numberPath;
	QuerydslPagingAdvancedItemReaderExpression expression;
	N currentId;
	String fieldName;

	public QuerydslPagingAdvancedItemReaderOption(NumberPath<N> numberPath, QuerydslPagingAdvancedItemReaderExpression expression) {
		String[] qField = numberPath.toString().split("\\.");
		this.fieldName = qField[qField.length - 1];

		this.numberPath = numberPath;
		this.expression = expression;
	}

	public OrderSpecifier<N> orderExpression() {
		return expression.order(numberPath);
	}

	public BooleanExpression whereExpression() {
		return  expression.where(numberPath, currentId);
	}

	protected Object getFieldValue(T item) {
		try {
			Field field = item.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(item);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalArgumentException("Not Found or Not Access Field");
		}
	}

	public void resetCurrentId(T item) {
		currentId = (N) getFieldValue(item);
	}
}
