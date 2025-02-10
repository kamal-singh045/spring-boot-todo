package springboot_todo.todo.specification;

import org.springframework.data.jpa.domain.Specification;

import springboot_todo.todo.dto.GetAllUsersDto;
import springboot_todo.todo.entity.UserEntity;
import springboot_todo.todo.enums.RoleEnum;

public class UserSpecification {
	public static Specification<UserEntity> filtersUsers(GetAllUsersDto input) {
		return (root, query, criticalBuilder) -> {
			Specification<UserEntity> spec = Specification.where(null);
			spec = spec.and((r, q, cb) -> cb.equal(r.get("role"), RoleEnum.USER));
			if (input.getStatus() != null) {
				spec = spec.and((r, q, cb) -> cb.equal(r.get("status"), input.getStatus()));
			}
			if (input.getSearchString() != null && !input.getSearchString().isEmpty()) {
				spec = spec.and((r, q, cb) -> cb.or(
						cb.like(cb.lower(r.get("email")), "%" + input.getSearchString().toLowerCase() + "%"),
						cb.like(cb.lower(r.get("name")), "%" + input.getSearchString().toLowerCase() + "%")));

			}
			return spec.toPredicate(root, query, criticalBuilder);
		};
	}
}
