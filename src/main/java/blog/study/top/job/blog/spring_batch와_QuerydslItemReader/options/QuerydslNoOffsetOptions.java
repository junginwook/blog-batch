package blog.study.top.job.blog.spring_batch와_QuerydslItemReader.options;

import java.beans.Expression;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class QuerydslNoOffsetOptions {
	protected Log logger = LogFactory.getLog(getClass());

	protected final String fieldName;
	protected final Expression expression;


}
