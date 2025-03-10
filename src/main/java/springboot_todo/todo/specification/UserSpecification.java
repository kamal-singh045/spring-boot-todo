package springboot_todo.todo.specification;

import org.springframework.data.jpa.domain.Specification;

import springboot_todo.todo.entity.UserEntity;
import springboot_todo.todo.enums.RoleEnum;
import springboot_todo.todo.enums.UserStatusEnum;

public class UserSpecification {
	private UserSpecification() {
	}

	public static Specification<UserEntity> filtersUsers(UserStatusEnum status, String searchString) {
		return (root, query, criticalBuilder) -> {
			Specification<UserEntity> spec = Specification.where(null);
			spec = spec.and((r, q, cb) -> cb.equal(r.get("role"), RoleEnum.USER));
			if (status != null) {
				spec = spec.and((r, q, cb) -> cb.equal(r.get("status"), status));
			}
			if (searchString != null && !searchString.isEmpty()) {
				spec = spec.and((r, q, cb) -> cb.or(
						cb.like(cb.lower(r.get("email")), "%" + searchString.toLowerCase() + "%"),
						cb.like(cb.lower(r.get("name")), "%" + searchString.toLowerCase() + "%")));

			}
			return spec.toPredicate(root, query, criticalBuilder);
		};
	}
}
