package nl.andrewl.worlds_of_beleriand.server.api.world.action;

import lombok.RequiredArgsConstructor;
import nl.andrewl.worlds_of_beleriand.server.api.world.action.dto.ActionData;
import nl.andrewl.worlds_of_beleriand.server.model.user.User;
import nl.andrewl.worlds_of_beleriand.server.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/world/actions")
@RequiredArgsConstructor
public class ActionsController {
	private final UserService userService;

	@GetMapping
	public List<ActionData> getAvailableActions(User user) {
		return this.userService.getAvailableActions(user.getId());
	}
}
