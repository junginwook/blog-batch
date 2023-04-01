package blog.study.top.job.blog.itemWriter에_list_전달하기;

import jakarta.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.database.JpaItemWriter;

public class ListJpaPagingItemWriter<T> extends JpaItemWriter<List<T>> {
	private JpaItemWriter<T> jpaItemWriter;

	public ListJpaPagingItemWriter(JpaItemWriter<T> jpaItemWriter, EntityManagerFactory entityManagerFactory) {
		this.jpaItemWriter = jpaItemWriter;
		this.setEntityManagerFactory(entityManagerFactory);
	}

	@Override
	public void write(Chunk<? extends List<T>> items) {
		List<T> totalList = new ArrayList<>();

		for(List<T> list: items) {
			totalList.addAll(list);
		}

		jpaItemWriter.write(new Chunk<T>(totalList));
	}
}
