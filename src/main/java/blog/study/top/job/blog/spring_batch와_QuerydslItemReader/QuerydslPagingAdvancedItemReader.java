package blog.study.top.job.blog.spring_batch와_QuerydslItemReader;

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
	private JPAQueryFactory queryFactory;
	private boolean transacted = true;
	private QuerydslPagingAdvancedItemReaderOption option;

	public QuerydslPagingAdvancedItemReader() {
		setName(ClassUtils.getShortName(QuerydslPagingAdvancedItemReader.class));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
	}

	@Override
	protected void doOpen() throws Exception {
		super.doOpen();

		entityManager = entityManagerFactory.createEntityManager(jpaPropertyMap);
		queryFactory = new JPAQueryFactory(entityManager);
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
			items = queryFunction.apply(queryFactory)
					.orderBy(option.orderExpression())
					.limit(getPageSize()).fetch();
		}
		else {
			items = queryFunction.apply(queryFactory)
					.where(option.whereExpression())
					.orderBy(option.orderExpression())
					.limit(getPageSize()).fetch();
		}

		if (items.size() > 0) {
			option.resetCurrentId(items.get(items.size() - 1));
			results.addAll(items);
		} else {
			results.clear();
		}

		tx.commit();
	}

	@Override
	protected void doClose() throws Exception {
		entityManager.close();
		super.doClose();
	}

	@Override
	protected void jumpToItem(int itemIndex) throws Exception {
		option.resetCurrentIdByInt(itemIndex);
		super.jumpToItem(itemIndex);
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
