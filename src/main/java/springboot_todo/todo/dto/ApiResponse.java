package springboot_todo.todo.dto;

import lombok.*;

@Getter
@Setter
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private PaginationResponse pagination;

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // this constructor can be generated by lombok also but here I have written
    // manually
    public ApiResponse(boolean success, String message, T data, PaginationResponse pagination) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.pagination = pagination;
    }
}
