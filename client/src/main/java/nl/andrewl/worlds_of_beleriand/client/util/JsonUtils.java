package nl.andrewl.worlds_of_beleriand.client.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JsonUtils {
	public static final ObjectMapper mapper = new ObjectMapper();

	public static String writeString(Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static <T> T read(InputStream in, Class<T> type) {
		try {
			return mapper.readValue(in, type);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
