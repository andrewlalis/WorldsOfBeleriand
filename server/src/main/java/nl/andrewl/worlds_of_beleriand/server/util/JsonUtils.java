package nl.andrewl.worlds_of_beleriand.server.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Some utility methods to ease the burden when handling JSON.
 */
@Slf4j
public class JsonUtils {
	private static ObjectMapper mapper;

	public static ObjectMapper getMapper() {
		if (mapper == null) {
			mapper = new ObjectMapper();
		}
		return mapper;
	}

	public static JsonNode read(String jsonString) {
		try {
			return getMapper().readTree(jsonString);
		} catch (JsonProcessingException e) {
			log.warn("JSON processing exception: {}", e.getMessage());
			return null;
		}
	}

	public static <T> T read(String jsonString, Class<T> objectClass) {
		try {
			return getMapper().readValue(jsonString, objectClass);
		} catch (JsonProcessingException e) {
			log.warn("JSON processing exception: {}", e.getMessage());
			return null;
		}
	}

	public static void write(OutputStream os, Object obj) {
		try {
			getMapper().writeValue(os, obj);
		} catch (IOException e) {
			log.warn("IOException while writing JSON value: {}", e.getMessage());
		}
	}

	public static void write(HttpServletResponse response, int statusCode, Object obj) {
		try {
			response.setStatus(statusCode);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			getMapper().writeValue(response.getOutputStream(), obj);
			response.flushBuffer();
		} catch (IOException e) {
			log.warn("IOException while writing JSON to HttpServletResponse: {}", e.getMessage());
		}
	}
}
