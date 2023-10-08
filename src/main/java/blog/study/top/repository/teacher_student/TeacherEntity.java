package blog.study.top.repository.teacher_student;

import blog.study.top.repository.product.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@NoArgsConstructor
@Table(name = "teacher")
public class TeacherEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	public String name;

	@OneToMany(fetch = FetchType.LAZY)
	@Cascade(CascadeType.PERSIST)
	public List<StudentEntity> students = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	public Product product;

	public TeacherEntity(String teacherName) {
		this.name = teacherName;
	}

	public void addStudent(StudentEntity student) {
		students.add(student);
	}
}
