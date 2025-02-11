package springboot_todo.todo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import springboot_todo.todo.dto.ApiResponse;
import springboot_todo.todo.dto.GetAllUsersDto;
import springboot_todo.todo.dto.GetAllUsersResponse;
import springboot_todo.todo.dto.UpdateUserStatusDto;
import springboot_todo.todo.enums.RoleEnum;
import springboot_todo.todo.enums.UserStatusEnum;
import springboot_todo.todo.exception.CustomException;
import springboot_todo.todo.security.AllowedRoles;
import springboot_todo.todo.service.UserService;
import springboot_todo.todo.utils.Constants;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    // constructor based injection
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @AllowedRoles(value = { RoleEnum.ADMIN })
    @PutMapping("/update-status")
    public ResponseEntity<ApiResponse<?>> updateUserStatus(@RequestBody UpdateUserStatusDto input) {
        try {
            String message = this.userService.updateUserStatus(input.getUserId(), input.getStatus());
            return ResponseEntity.ok(new ApiResponse<>(true, message, null));
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @AllowedRoles(value = { RoleEnum.ADMIN })
    @GetMapping
    public ResponseEntity<ApiResponse<List<GetAllUsersResponse>>> getAllUsers(
            @RequestParam(required = false, defaultValue = Constants.Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Constants.Pagination.DEFAULT_PAGE) int limit,
            @RequestParam(required = false) UserStatusEnum status,
            @RequestParam(required = false) String searchString) {
        try {
            GetAllUsersDto input = new GetAllUsersDto(status, searchString);
            ApiResponse<List<GetAllUsersResponse>> usersList = this.userService.getAllUsers(input, page, limit);
            return ResponseEntity.ok(usersList);
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
