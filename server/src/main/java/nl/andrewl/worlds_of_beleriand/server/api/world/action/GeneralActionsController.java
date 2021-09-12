package nl.andrewl.worlds_of_beleriand.server.api.world.action;

import lombok.RequiredArgsConstructor;
import nl.andrewl.worlds_of_beleriand.server.api.world.action.dto.ActionResponse;
import nl.andrewl.worlds_of_beleriand.server.dao.UserRepository;
import nl.andrewl.worlds_of_beleriand.server.model.user.User;
import nl.andrewl.worlds_of_beleriand.server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/world/actions")
@RequiredArgsConstructor
public class GeneralActionsController {
	private final UserService userService;
	private final UserRepository userRepository;

	@PostMapping(path = "/exit")
	public ActionResponse handleExitAction(User user) {
		this.userService.goOffline(user);
		return ActionResponse.success("You have successfully logged off.");
	}

	@PostMapping(path = "/examine")
	@Transactional(readOnly = true)
	public ActionResponse handleExamineAction(User u) {
		var user = this.userRepository.findById(u.getId()).orElseThrow(); // Do this so the user is handled by the session.
		List<User> otherUsers = this.userRepository.findAllByLocationAndOnlineIsTrue(user.getLocation());
		otherUsers.remove(user);

		StringBuilder sb = new StringBuilder();
		sb.append("You inspect your surroundings...\n")
				.append("You're currently in ").append(user.getLocation().getName())
				.append(": ").append(user.getLocation().getDescription()).append("\n");
		sb.append("There are pathways which you can follow to the following locations:\n");
		for (var adj : user.getLocation().getAdjacentLocations().stream().sorted().toList()) {
			sb.append(adj.getName()).append("\n");
		}
		if (!otherUsers.isEmpty()) {
			sb.append("There are some other people here:\n");
			for (var other : otherUsers) {
				sb.append(other.getName()).append("\n");
			}
		}
		return ActionResponse.success(sb.toString());
	}

	@PostMapping(path = "/goto")
	@Transactional
	public ActionResponse handleGotoAction(User user, @RequestBody Map<String, String> options) {
		String destination = options.get("destination");
		if (destination == null || destination.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid destination.");
		return this.userService.goTo(user, destination);
	}
}
