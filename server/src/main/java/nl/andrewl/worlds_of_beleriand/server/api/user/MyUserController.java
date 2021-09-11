package nl.andrewl.worlds_of_beleriand.server.api.user;

import lombok.RequiredArgsConstructor;
import nl.andrewl.worlds_of_beleriand.server.api.user.dto.UserData;
import nl.andrewl.worlds_of_beleriand.server.model.user.User;
import nl.andrewl.worlds_of_beleriand.server.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users/me")
@RequiredArgsConstructor
public class MyUserController {
	private final UserService userService;

	@GetMapping
	public UserData getMyUserData(Authentication auth) {
		var user = (User) auth.getPrincipal();
		return new UserData(user);
	}
}
