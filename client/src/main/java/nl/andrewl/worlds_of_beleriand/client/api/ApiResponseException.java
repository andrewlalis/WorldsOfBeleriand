package nl.andrewl.worlds_of_beleriand.client.api;

import lombok.Getter;

public class ApiResponseException extends RuntimeException {
	@Getter
	private final int code;

	public ApiResponseException(int code, String message) {
		super(message);
		this.code = code;
	}
}
