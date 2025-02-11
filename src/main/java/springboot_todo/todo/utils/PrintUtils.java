package springboot_todo.todo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.Arrays;
import java.util.List;

public class PrintUtils {
	private static final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

	public static void print(Object data) {
		if (data == null) {
			System.out.println("Null Value");
			return;
		}

		try {
			if (data instanceof String || data instanceof Number || data instanceof Boolean) {
				System.out.println(data);
			} else if (data.getClass().isArray()) {
				System.out.println(Arrays.toString((Object[]) data));
			} else if (data instanceof List) {
				System.out.println(objectMapper.writeValueAsString(data));
			} else {
				System.out.println(objectMapper.writeValueAsString(data));
			}
		} catch (Exception e) {
			System.out.println("Error printing object: " + e.getMessage());
		}
	}
}
