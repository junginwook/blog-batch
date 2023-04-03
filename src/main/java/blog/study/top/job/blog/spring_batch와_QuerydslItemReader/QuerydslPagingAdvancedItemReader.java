package blog.study.top.job.blog.spring_batch와_QuerydslItemReader;


import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.HashMap;
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
	private Map<String, Object> parameterValues;
	private JPAQuery<T> firstQuery;
	private boolean transacted = true;

	public QuerydslPagingAdvancedItemReader() {
		setName(ClassUtils.getShortName(QuerydslPagingAdvancedItemReader.class));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		firstQuery = queryFunction.apply(queryFactory)
				.limit(getPageSize());
	}

//	private OrderSpecifier<Integer> orderExpression() {
//		return expression.order(field);
//	}

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
		if(results == null) {
			results = new CopyOnWriteArrayList<>();
		}
		else {
			results.clear();
		}



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

	public void setParameterValues(Map<String, Object> parameterValues) {
		this.parameterValues = parameterValues;
	}

	public void setTransacted(boolean transacted) {
		this.transacted = transacted;
	}
}
