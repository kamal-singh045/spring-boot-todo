package springboot_todo.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import springboot_todo.todo.enums.TodoStatusEnum;

@Getter
@Setter
@AllArgsConstructor
public class GetAllTodosDto {
	private TodoStatusEnum status;
	private String searchString;
}
