package springboot_todo.todo.specification;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import springboot_todo.todo.entity.TodoEntity;
import springboot_todo.todo.enums.TodoStatusEnum;

public class TodoSpecification {
	private TodoSpecification() {
	}

	public static Specification<TodoEntity> filtersTodos(TodoStatusEnum status, String searchString, UUID userId) {
		return (root, query, criticalBuilder) -> {
			Specification<TodoEntity> spec = Specification.where(null);
			spec = spec.and((r, q, cb) -> cb.equal(r.get("user").get("id"), userId));
			if (status != null) {
				spec = spec.and((r, q, cb) -> cb.equal(r.get("status"), status));
			}
			if (searchString != null && !searchString.isEmpty()) {
				spec = spec.and((r, q, cb) -> cb.or(
						cb.like(cb.lower(r.get("title")), "%" + searchString.toLowerCase() + "%"),
						cb.like(cb.lower(r.get("description")), "%" + searchString.toLowerCase() + "%")));
			}
			return spec.toPredicate(root, query, criticalBuilder);
		};
	}
}
