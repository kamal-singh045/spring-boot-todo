package springboot_todo.todo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springboot_todo.todo.dto.ApiResponse;
import springboot_todo.todo.dto.LoginDto;
import springboot_todo.todo.entity.UserEntity;
import springboot_todo.todo.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;

    // constructor based injection
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    private ResponseEntity<ApiResponse<?>> register(@RequestBody UserEntity userData) {
        String token = this.userService.register(userData);
        return ResponseEntity.ok(new ApiResponse<>(true, "Registration Successful", token));
    }

    @PostMapping("/login")
    private ResponseEntity<ApiResponse<?>> login(@RequestBody LoginDto loginData) {
        String token = this.userService.login(loginData);
        return ResponseEntity.ok(new ApiResponse<>(true, "Login Successful", token));
    }
}
