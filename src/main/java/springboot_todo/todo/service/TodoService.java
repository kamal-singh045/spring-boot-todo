package springboot_todo.todo.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import springboot_todo.todo.dto.GetAllTodosDto;
import springboot_todo.todo.entity.TodoEntity;
import springboot_todo.todo.entity.UserEntity;
import springboot_todo.todo.exception.CustomException;
import springboot_todo.todo.repository.TodoRepository;
import springboot_todo.todo.repository.UserRepository;
import springboot_todo.todo.specification.TodoSpecification;

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
        return this.todoRepository.save(data);
    }

    public List<TodoEntity> getAllTodos(GetAllTodosDto input, UUID userId) {
        Specification<TodoEntity> spec = TodoSpecification.filtersTodos(input, userId);
        return this.todoRepository.findAll(spec);
    }

    public TodoEntity getTodoById(UUID id) {
        return this.todoRepository.findById(id).orElseThrow(() -> new CustomException(
                "Todo with id: %s is not found!!!".formatted(String.valueOf(id)),
                HttpStatus.NOT_FOUND));
    }

    public TodoEntity updateTodo(UUID id, TodoEntity data, UUID userId) {
        if (id == null) {
            throw new CustomException(
                    "Todo id is required to update the todo.",
                    HttpStatus.BAD_REQUEST);
        }
        TodoEntity todo = todoRepository.findById(id).orElseThrow(() -> new CustomException(
                "Todo with id: %s is not found!!!".formatted(String.valueOf(id)),
                HttpStatus.NOT_FOUND));
        if (!todo.getUser().getId().equals(userId)) {
            throw new CustomException(
                    "You are not authorized to update other's todo",
                    HttpStatus.FORBIDDEN);
        }
        todo.setTitle(data.getTitle());
        todo.setDescription(data.getDescription());
        todo.setStatus(data.getStatus());
        return this.todoRepository.save(todo);
    }

    public String deleteTodo(UUID id, UUID userId) {
        if (id == null) {
            throw new CustomException(
                    "Todo id is required to delete the todo.",
                    HttpStatus.BAD_REQUEST);
        }
        TodoEntity todo = this.todoRepository.findById(id).orElseThrow(() -> new CustomException(
                "Todo with id: %s is not found!!!".formatted(String.valueOf(id)),
                HttpStatus.NOT_FOUND));
        if (todo == null) {
            throw new CustomException(
                    "Todo not found to delete",
                    HttpStatus.NOT_FOUND);
        } else if (!todo.getUser().getId().equals(userId)) {
            throw new CustomException(
                    "You are not authorized to delete other's todo",
                    HttpStatus.FORBIDDEN);
        }

        this.todoRepository.deleteById(id);
        return "Todo deleted successfully";
    }
}
