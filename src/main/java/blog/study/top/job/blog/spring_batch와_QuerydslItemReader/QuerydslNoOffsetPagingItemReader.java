package blog.study.top.job.blog.spring_batch와_QuerydslItemReader;

import blog.study.top.job.blog.spring_batch와_QuerydslItemReader.options.QuerydslNoOffsetOptions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.function.Function;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

public class QuerydslNoOffsetPagingItemReader<T> extends QuerydslPagingItemReader<T> {
	private QuerydslNoOffsetOptions<T> options;

	private QuerydslNoOffsetPagingItemReader() {
		super();
		setName(ClassUtils.getShortName(QuerydslNoOffsetPagingItemReader.class));
	}

	public QuerydslNoOffsetPagingItemReader(EntityManagerFactory entityManagerFactory,
			int pageSize,
			QuerydslNoOffsetOptions<T> options,
			Function<JPAQueryFactory, JPAQuery<T>> queryFunction) {
		super(entityManagerFactory, pageSize, queryFunction);
		setName(ClassUtils.getShortName(QuerydslNoOffsetPagingItemReader.class));
		this.options = options;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		super.afterPropertiesSet();
	}

	@Override
	protected void doReadPage() {

		EntityTransaction tx = getTxOrNull();

		JPQLQuery<T> query = createQuery().limit(getPageSize());

		initResults();

		fetchQuery(query, tx);

		resetCurrentIdIfNotLastPage();
	}

	@Override
	protected JPAQuery<T> createQuery() {
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		JPAQuery<T> query = queryFunction.apply(queryFactory);
		options.initKeys(query, getPage());

		return options.createQuery(query, getPage());
	}

	private void resetCurrentIdIfNotLastPage() {
		if (isNotEmptyResults()) {
			options.resetCurrentId(getLastTime());
		}
	}

	private boolean isNotEmptyResults() {
		return !CollectionUtils.isEmpty(results) && results.get(0) != null;
	}

	private T getLastTime() {
		return results.get(results.size() - 1);
	}
}
