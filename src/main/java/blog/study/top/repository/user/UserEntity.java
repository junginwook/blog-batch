package blog.study.top.repository.user;

import blog.study.top.repository.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user")
public class UserEntity extends BaseEntity {

	@Id
	private String userId;

	private String userName;
	@Enumerated(EnumType.STRING)
	private UserStatus status;
	private String phone;
}
