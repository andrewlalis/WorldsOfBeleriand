package nl.andrewl.worlds_of_beleriand.server.api;

import nl.andrewl.worlds_of_beleriand.server.util.JsonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler
	public void handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {
		if (e instanceof ResponseStatusException rse) {
			String reason = rse.getReason() != null ? rse.getReason() : "";
			JsonUtils.write(response, rse.getRawStatusCode(), Map.of("message", reason));
		} else {
			e.printStackTrace();
			JsonUtils.write(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), Map.of("message", "An error occurred."));
		}
	}
}
