package springboot_todo.todo.specification;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import springboot_todo.todo.dto.GetAllTodosDto;
import springboot_todo.todo.entity.TodoEntity;

public class TodoSpecification {
	public static Specification<TodoEntity> filtersTodos(GetAllTodosDto input, UUID userId) {
		return (root, query, criticalBuilder) -> {
			Specification<TodoEntity> spec = Specification.where(null);
			spec = spec.and((r, q, cb) -> cb.equal(r.get("user").get("id"), userId));
			if (input.getStatus() != null) {
				spec = spec.and((r, q, cb) -> cb.equal(r.get("status"), input.getStatus()));
			}
			if (input.getSearchString() != null && !input.getSearchString().isEmpty()) {
				spec = spec.and((r, q, cb) -> cb.or(
						cb.like(cb.lower(r.get("title")), "%" + input.getSearchString().toLowerCase() + "%"),
						cb.like(cb.lower(r.get("description")), "%" + input.getSearchString().toLowerCase() + "%")));
			}
			return spec.toPredicate(root, query, criticalBuilder);
		};
	}
}
