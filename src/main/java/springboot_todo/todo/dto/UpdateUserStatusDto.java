package springboot_todo.todo.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springboot_todo.todo.enums.UserStatusEnum;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateUserStatusDto {
	private UUID userId;
	private UserStatusEnum status;
}
