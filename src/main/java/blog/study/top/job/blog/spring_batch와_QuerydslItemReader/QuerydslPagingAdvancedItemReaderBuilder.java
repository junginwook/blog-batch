package blog.study.top.job.blog.spring_batch와_QuerydslItemReader;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManagerFactory;
import java.util.Map;
import java.util.function.Function;

public class QuerydslPagingAdvancedItemReaderBuilder<T> {

	private int pageSize = 10;

	private EntityManagerFactory entityManagerFactory;

	private boolean transacted = true;

	private String name;

	private int currentItemCount;

	private int maxItemCount = Integer.MAX_VALUE;

	private boolean saveState;

	private QuerydslPagingAdvancedItemReaderOption option;

	private Function<JPAQueryFactory, JPAQuery<T>> queryFunction;


	public QuerydslPagingAdvancedItemReaderBuilder<T> name(String name) {
		this.name = name;

		return this;
	}

	public QuerydslPagingAdvancedItemReaderBuilder<T> transacted(boolean transacted) {
		this.transacted = transacted;

		return this;
	}

	public QuerydslPagingAdvancedItemReaderBuilder<T> currentItemCount(int currentItemCount) {
		this.currentItemCount = currentItemCount;

		return this;
	}

	public QuerydslPagingAdvancedItemReaderBuilder<T> maxItemCount(int maxItemCount) {
		this.maxItemCount = maxItemCount;

		return this;
	}

	public QuerydslPagingAdvancedItemReaderBuilder<T> pageSize(int pageSize) {
		this.pageSize = pageSize;

		return this;
	}

	public QuerydslPagingAdvancedItemReaderBuilder<T> entityManagerFactory(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;

		return this;
	}

	public QuerydslPagingAdvancedItemReaderBuilder<T> saveStage(boolean saveState) {
		this.saveState = saveState;

		return this;
	}

	public QuerydslPagingAdvancedItemReaderBuilder<T> queryFunction(Function<JPAQueryFactory, JPAQuery<T>> queryFunction) {
		this.queryFunction = queryFunction;

		return this;
	}

	public QuerydslPagingAdvancedItemReaderBuilder<T> option(QuerydslPagingAdvancedItemReaderOption option) {
		this.option = option;

		return this;
	}

	public QuerydslPagingAdvancedItemReader<T> build() {

		QuerydslPagingAdvancedItemReader<T> reader = new QuerydslPagingAdvancedItemReader<>();
		reader.setName(name);
		reader.setPageSize(pageSize);
		reader.setCurrentItemCount(currentItemCount);
		reader.setMaxItemCount(maxItemCount);
		reader.setSaveState(saveState);
		reader.setTransacted(transacted);
		reader.setEntityManagerFactory(entityManagerFactory);
		reader.setQueryFunction(queryFunction);
		reader.setOption(option);

		return reader;
	}
}
