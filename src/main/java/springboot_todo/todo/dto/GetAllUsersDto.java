package springboot_todo.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import springboot_todo.todo.enums.UserStatusEnum;

@Getter
@Setter
@AllArgsConstructor
public class GetAllUsersDto {
	private UserStatusEnum status;
	private String searchString;
}
