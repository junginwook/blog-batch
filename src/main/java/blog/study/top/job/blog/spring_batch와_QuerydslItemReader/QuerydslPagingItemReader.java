package blog.study.top.job.blog.spring_batch와_QuerydslItemReader;

import com.querydsl.jpa.JPQLQuery;
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
import org.springframework.util.CollectionUtils;

public class QuerydslPagingItemReader<T> extends AbstractPagingItemReader<T> {

	protected final Map<String, Object> jpaPropertyMap = new HashMap<>();
	protected EntityManagerFactory entityManagerFactory;
	protected EntityManager entityManager;
	protected Function<JPAQueryFactory, JPAQuery<T>> queryFunction;
	protected boolean transacted = true;

	public QuerydslPagingItemReader() {
		setName(ClassUtils.getShortName(QuerydslPagingItemReader.class));
	}

	public QuerydslPagingItemReader(EntityManagerFactory entityManagerFactory,
			int pageSize,
			Function<JPAQueryFactory, JPAQuery<T>> queryFunction) {
		this();
		this.entityManagerFactory = entityManagerFactory;
		this.queryFunction = queryFunction;
		setPageSize(pageSize);
	}

	public void setTransacted(boolean transacted) {
		this.transacted = transacted;
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
	@SuppressWarnings("uncheckd")
	protected void doReadPage() {
		EntityTransaction tx = getTxOrNull();

		JPAQuery<T> query = createQuery()
				.offset(getPage() * getPageSize())
				.limit(getPageSize());

		initResults();

		fetchQuery(query, tx);
	}

	protected EntityTransaction getTxOrNull() {
		if (transacted) {
			EntityTransaction tx = entityManager.getTransaction();
			tx.begin();

			entityManager.flush();
			entityManager.clear();
			return tx;
		}

		return null;
	}

	protected JPAQuery<T> createQuery() {
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		return queryFunction.apply(queryFactory);
	}

	protected void initResults() {
		if (CollectionUtils.isEmpty(results)) {
			results = new CopyOnWriteArrayList<>();
		} else {
			results.clear();
		}
	}

	protected void fetchQuery(JPQLQuery<T> query, EntityTransaction tx) {
		if (transacted) {
			results.addAll(query.fetch());
			if(tx != null) {
				tx.commit();
			}
		} else {
			List<T> queryResult = query.fetch();
			for (T entity : queryResult) {
				entityManager.detach(entity);
				results.add(entity);
			}
		}
	}

	@Override
	protected void doClose() throws Exception {
		entityManager.clear();
		super.doClose();
	}
}
