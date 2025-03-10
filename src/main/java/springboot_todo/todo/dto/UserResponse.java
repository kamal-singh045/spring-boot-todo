package springboot_todo.todo.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import springboot_todo.todo.enums.UserStatusEnum;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
	private UUID id;
	private String name;
	private String email;
	private UserStatusEnum status;
}
