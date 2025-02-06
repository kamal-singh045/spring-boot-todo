package springboot_todo.todo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import springboot_todo.todo.dto.ApiResponse;
import springboot_todo.todo.entity.TodoEntity;
import springboot_todo.todo.entity.UserEntity;
import springboot_todo.todo.enums.RoleEnum;
import springboot_todo.todo.exception.CustomException;
import springboot_todo.todo.security.AllowedRoles;
import springboot_todo.todo.service.TodoService;

import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    // constructor based injection
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @AllowedRoles(value = { RoleEnum.USER })
    @PostMapping()
    public ResponseEntity<ApiResponse<TodoEntity>> createTodo(
            @RequestBody TodoEntity data,
            @AuthenticationPrincipal UserEntity user) {
        try {
            UUID userId = user.getId();
            TodoEntity newTodo = todoService.createTodo(data, userId);
            System.out.print(newTodo);
            return ResponseEntity.ok(new ApiResponse<>(true, "Todo created successfully", newTodo));
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @AllowedRoles(value = { RoleEnum.USER })
    @GetMapping()
    public ResponseEntity<ApiResponse<List<TodoEntity>>> getAllTodos() {
        try {
            List<TodoEntity> allTodos = this.todoService.getAllTodos();
            return ResponseEntity.ok(new ApiResponse<>(true, "All Todos", allTodos));
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @AllowedRoles(value = { RoleEnum.USER })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoEntity>> getTodoById(@PathVariable UUID id) {
        try {
            TodoEntity todo = todoService.getTodoById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Todo fetched", todo));
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @AllowedRoles(value = { RoleEnum.USER })
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<TodoEntity>> updateTodo(@RequestBody TodoEntity data) {
        try {
            UUID todoId = data.getId();
            TodoEntity todo = todoService.updateTodo(todoId, data);
            return ResponseEntity.ok(new ApiResponse<>(true, "Todo updated", todo));
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
