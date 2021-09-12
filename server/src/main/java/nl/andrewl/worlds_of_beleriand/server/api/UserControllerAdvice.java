package nl.andrewl.worlds_of_beleriand.server.api;

import nl.andrewl.worlds_of_beleriand.server.model.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserControllerAdvice {
	@ModelAttribute
	public User userPrincipal(Authentication auth) {
		return (User) (auth == null ? null : auth.getPrincipal());
	}
}
