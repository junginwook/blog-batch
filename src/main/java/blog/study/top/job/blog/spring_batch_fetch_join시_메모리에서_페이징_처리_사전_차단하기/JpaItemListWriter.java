package blog.study.top.job.blog.spring_batch_fetch_join시_메모리에서_페이징_처리_사전_차단하기;

import java.util.ArrayList;
import java.util.List;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.database.JpaItemWriter;

public class JpaItemListWriter<T> extends JpaItemWriter<List<T>> {
	private JpaItemWriter<T> jpaItemWriter;

	public JpaItemListWriter(JpaItemWriter<T> jpaItemWriter) {
		this.jpaItemWriter = jpaItemWriter;
	}

	@Override
	public void write(Chunk<? extends List<T>> items) {
		List<T> totalList = new ArrayList<>();

		for(List<T> list : items) {
			totalList.addAll(list);
		}

		jpaItemWriter.write(new Chunk<T>(totalList));
	}
}
