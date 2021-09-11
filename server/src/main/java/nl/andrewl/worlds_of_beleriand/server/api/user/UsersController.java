package nl.andrewl.worlds_of_beleriand.server.api.user;

import lombok.RequiredArgsConstructor;
import nl.andrewl.worlds_of_beleriand.server.api.user.dto.UserData;
import nl.andrewl.worlds_of_beleriand.server.api.user.dto.UserRegistrationPayload;
import nl.andrewl.worlds_of_beleriand.server.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UsersController {
	private final UserService userService;

	@PostMapping(path = "/register")
	public UserData register(@Valid @RequestBody UserRegistrationPayload payload) {
		var user = this.userService.registerNewUser(payload);
		return new UserData(user);
	}
}
