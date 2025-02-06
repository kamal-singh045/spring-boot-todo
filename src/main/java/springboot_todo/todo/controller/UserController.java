package springboot_todo.todo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springboot_todo.todo.dto.ApiResponse;
import springboot_todo.todo.dto.LoginDto;
import springboot_todo.todo.dto.UpdateUserStatusDto;
import springboot_todo.todo.entity.UserEntity;
import springboot_todo.todo.enums.RoleEnum;
import springboot_todo.todo.enums.UserStatusEnum;
import springboot_todo.todo.exception.CustomException;
import springboot_todo.todo.security.AllowedRoles;
import springboot_todo.todo.service.UserService;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    // constructor based injection
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody UserEntity userData) {
        try {
            String token = this.userService.register(userData);
            return ResponseEntity.ok(new ApiResponse<>(true, "Registration Successful", token));
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register-admin")
    public ResponseEntity<ApiResponse<?>> registerAdmin(@RequestBody UserEntity userData) {
        try {
            userData.setRole(RoleEnum.ADMIN);
            userData.setStatus(UserStatusEnum.ACTIVE);
            String token = this.userService.register(userData);
            return ResponseEntity.ok(new ApiResponse<>(true, "Admin Registration Successful", token));
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginDto loginData) {
        try {
            String token = this.userService.login(loginData);
            return ResponseEntity.ok(new ApiResponse<>(true, "Login Successful", token));
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
}
