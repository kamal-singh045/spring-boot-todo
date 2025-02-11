package springboot_todo.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaginationResponse {
	private int currPage; // current page
	private int maxPage;
	private int limit; // limit
	private long totalCount; // total no of items
}
