package springboot_todo.todo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import springboot_todo.todo.dto.ApiResponse;
import springboot_todo.todo.entity.TodoEntity;
import springboot_todo.todo.entity.UserEntity;
import springboot_todo.todo.service.TodoService;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    // constructor based injection
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping()
    private ResponseEntity<ApiResponse<TodoEntity>> createTodo(
            @RequestBody TodoEntity data,
            @AuthenticationPrincipal UserEntity user
    ) {
        Long userId = user.getId();
        System.out.println("USER_ID "+ userId);
        TodoEntity newTodo = todoService.createTodo(data, userId);
        System.out.print(newTodo);
        return ResponseEntity.ok(new ApiResponse<>(true, "Todo created successfully", newTodo));
    }

    @GetMapping()
    private ResponseEntity<ApiResponse<List<TodoEntity>>> getAllTodos() {
        List<TodoEntity> allTodos = todoService.getAllTodos();
        return ResponseEntity.ok(new ApiResponse<>(true, "All Todos", allTodos));
    }

    @GetMapping("/{id}")
    private ResponseEntity<ApiResponse<TodoEntity>> getTodoById(@PathVariable Long id) {
        TodoEntity todo = todoService.getTodoById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Todo fetched", todo));
    }
}
