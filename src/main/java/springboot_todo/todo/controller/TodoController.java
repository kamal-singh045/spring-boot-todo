package springboot_todo.todo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import springboot_todo.todo.dto.ApiResponse;
import springboot_todo.todo.dto.GetAllTodosDto;
import springboot_todo.todo.entity.TodoEntity;
import springboot_todo.todo.entity.UserEntity;
import springboot_todo.todo.enums.RoleEnum;
import springboot_todo.todo.enums.TodoStatusEnum;
import springboot_todo.todo.security.AllowedRoles;
import springboot_todo.todo.service.TodoService;
import springboot_todo.todo.utils.Constants;

import java.util.List;
import java.util.UUID;

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
            TodoEntity newTodo = this.todoService.createTodo(data, userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Todo created successfully", newTodo));
        } catch (Exception e) {
            throw e;
        }
    }

    @AllowedRoles(value = { RoleEnum.USER })
    @GetMapping()
    public ResponseEntity<ApiResponse<List<TodoEntity>>> getAllTodos(
            @RequestParam(required = false, defaultValue = Constants.Pagination.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = Constants.Pagination.DEFAULT_LIMIT) int limit,
            @RequestParam(required = false) TodoStatusEnum status,
            @RequestParam(required = false) String searchString,
            @AuthenticationPrincipal UserEntity user) {
        try {
            UUID userId = user.getId();
            GetAllTodosDto input = new GetAllTodosDto(status, searchString);
            ApiResponse<List<TodoEntity>> allTodos = this.todoService.getAllTodos(input, page, limit, userId);
            return ResponseEntity.ok(allTodos);
        } catch (Exception e) {
            throw e;
        }
    }

    @AllowedRoles(value = { RoleEnum.USER })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoEntity>> getTodoById(@PathVariable UUID id) {
        try {
            TodoEntity todo = this.todoService.getTodoById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Todo fetched", todo));
        } catch (Exception e) {
            throw e;
        }
    }

    @AllowedRoles(value = { RoleEnum.USER })
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<TodoEntity>> updateTodo(
            @RequestBody TodoEntity data,
            @AuthenticationPrincipal UserEntity user) {
        try {
            UUID userId = user.getId();
            UUID todoId = data.getId();
            TodoEntity todo = this.todoService.updateTodo(todoId, data, userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Todo updated", todo));
        } catch (Exception e) {
            throw e;
        }
    }

    @AllowedRoles(value = { RoleEnum.USER })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteTodo(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserEntity user) {
        try {
            UUID userId = user.getId();
            String message = this.todoService.deleteTodo(id, userId);
            return ResponseEntity.ok(new ApiResponse<>(false, message, null));
        } catch (Exception e) {
            throw e;
        }
    }
}
