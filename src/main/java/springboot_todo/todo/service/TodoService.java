package springboot_todo.todo.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import springboot_todo.todo.entity.TodoEntity;
import springboot_todo.todo.entity.UserEntity;
import springboot_todo.todo.exception.CustomException;
import springboot_todo.todo.repository.TodoRepository;
import springboot_todo.todo.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(
            TodoRepository todoRepository,
            UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    public TodoEntity createTodo(TodoEntity data, UUID userId) {
        UserEntity user = this.userRepository.findById(userId).orElseThrow(() -> new CustomException(
                "User not found",
                HttpStatus.NOT_FOUND));
        data.setUser(user);
        return todoRepository.save(data);
    }

    public List<TodoEntity> getAllTodos() {
        return todoRepository.findAll();
    }

    public TodoEntity getTodoById(UUID id) {
        return todoRepository.findById(id).orElseThrow(() -> new CustomException(
                "Todo with id: %s is not found!!!".formatted(String.valueOf(id)),
                HttpStatus.NOT_FOUND));
    }

    public TodoEntity updateTodo(UUID id, TodoEntity data) {
        if (id == null) {
            throw new CustomException(
                    "Todo id is required to update the todo.",
                    HttpStatus.BAD_REQUEST);
        }
        TodoEntity todo = todoRepository.findById(id).orElseThrow(() -> new CustomException(
                "Todo with id: %s is not found!!!".formatted(String.valueOf(id)),
                HttpStatus.NOT_FOUND));
        todo.setTitle(data.getTitle());
        todo.setDescription(data.getDescription());
        todo.setStatus(data.getStatus());
        return todoRepository.save(todo);
    }
}
