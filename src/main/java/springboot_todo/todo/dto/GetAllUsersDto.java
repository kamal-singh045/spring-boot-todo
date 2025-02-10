package springboot_todo.todo.dto;

import lombok.Getter;
import lombok.Setter;
import springboot_todo.todo.enums.UserStatusEnum;

@Getter
@Setter
public class GetAllUsersDto {
	private UserStatusEnum status;
	private String searchString;
}
