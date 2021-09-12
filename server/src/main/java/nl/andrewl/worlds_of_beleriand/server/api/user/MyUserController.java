package nl.andrewl.worlds_of_beleriand.server.api.user;

import lombok.RequiredArgsConstructor;
import nl.andrewl.worlds_of_beleriand.server.api.user.dto.UserData;
import nl.andrewl.worlds_of_beleriand.server.model.user.User;
import nl.andrewl.worlds_of_beleriand.server.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "/users/me")
@RequiredArgsConstructor
public class MyUserController {
	private final UserService userService;

	@GetMapping
	public UserData getMyUserData(User user) {
		return this.userService.getData(user.getId());
	}

	@PostMapping(path = "/online")
	public Map<String, String> goOnline(User user) {
		this.userService.goOnline(user);
		return Map.of("message", "You are now online!");
	}
}
