package blog.study.top.job.blog.spring_batch와_QuerydslItemReader;


import blog.study.top.job.blog.spring_batch와_QuerydslItemReader.expression.OrderExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.util.ClassUtils;

public class QuerydslPagingAdvancedItemReader<T> extends AbstractPagingItemReader<T> {

	private Function<JPAQueryFactory, JPAQuery<T>> queryFunction;
	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private final Map<String, Object> jpaPropertyMap = new HashMap<>();
	private JPAQuery<T> firstQuery;
	private JPAQuery<T> remainingQuery;

	private boolean transacted = true;
	private QuerydslPagingAdvancedItemReaderOption option;

	public QuerydslPagingAdvancedItemReader() {
		setName(ClassUtils.getShortName(QuerydslPagingAdvancedItemReader.class));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		firstQuery = queryFunction.apply(queryFactory)
				.limit(getPageSize()).orderBy(option.orderExpression());
		remainingQuery = queryFunction.apply(queryFactory)
				.limit(getPageSize()).orderBy(option.orderExpression());
	}

	@Override
	protected void doOpen() throws Exception {
		super.doOpen();

		entityManager = entityManagerFactory.createEntityManager(jpaPropertyMap);
		if (entityManager == null) {
			throw new DataAccessResourceFailureException("Unable to obtain an EntityManager");
		}
	}

	@Override
	protected void doReadPage() {
		EntityTransaction tx = null;

		if (transacted) {
			tx = entityManager.getTransaction();
			tx.begin();

			entityManager.flush();
			entityManager.clear();
		} // end if

		if(results == null) {
			results = new CopyOnWriteArrayList<>();
		}
		else {
			results.clear();
		}

		List<T> items;

		if (getPage() == 0) {
			items = firstQuery.fetch();
		}
		else {
			items = remainingQuery.where(option.whereExpression()).fetch();
		}

		option.resetCurrentId(items.get(items.size() - 1));
		results.addAll(items);


		tx.commit();
	}

	@Override
	protected void doClose() throws Exception {
		entityManager.close();
		super.doClose();
	}

	public void setQueryFunction(Function<JPAQueryFactory, JPAQuery<T>> queryFunction) {
		this.queryFunction = queryFunction;
	}

	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	public void setOption(QuerydslPagingAdvancedItemReaderOption option) {
		this.option = option;
	}

	public void setTransacted(boolean transacted) {
		this.transacted = transacted;
	}
}
