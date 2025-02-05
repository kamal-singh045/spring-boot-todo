package springboot_todo.todo.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import springboot_todo.todo.entity.TodoEntity;
import springboot_todo.todo.entity.UserEntity;
import springboot_todo.todo.exception.CustomException;
import springboot_todo.todo.repository.TodoRepository;
import springboot_todo.todo.repository.UserRepository;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(
            TodoRepository todoRepository,
            UserRepository userRepository
    ) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    public TodoEntity createTodo(TodoEntity data, Long userId) {
        UserEntity user = this.userRepository.findById(userId).orElseThrow(() -> new CustomException(
                "User not found",
                HttpStatus.NOT_FOUND
                ));
        data.setUser(user);
        System.out.print(data);
        return todoRepository.save(data);
    }

    public List<TodoEntity> getAllTodos() {
        return todoRepository.findAll();
    }

    public TodoEntity getTodoById(Long id) {
        return todoRepository.findById(id).orElseThrow(() -> new CustomException(
                "Todo with id: %d is not found!!!".formatted(id),
                HttpStatus.NOT_FOUND
        ));
    }
}
