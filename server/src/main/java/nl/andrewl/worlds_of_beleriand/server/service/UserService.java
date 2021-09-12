package nl.andrewl.worlds_of_beleriand.server.service;

import lombok.RequiredArgsConstructor;
import nl.andrewl.worlds_of_beleriand.server.api.user.dto.UserData;
import nl.andrewl.worlds_of_beleriand.server.api.user.dto.UserRegistrationPayload;
import nl.andrewl.worlds_of_beleriand.server.api.world.action.dto.ActionData;
import nl.andrewl.worlds_of_beleriand.server.api.world.action.dto.ActionResponse;
import nl.andrewl.worlds_of_beleriand.server.dao.LocationRepository;
import nl.andrewl.worlds_of_beleriand.server.dao.UserRepository;
import nl.andrewl.worlds_of_beleriand.server.model.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final LocationRepository locationRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public User registerNewUser(UserRegistrationPayload payload) {
		if (this.userRepository.existsByName(payload.getUsername())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is taken.");
		if (payload.getPassword().length() < 8) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is too weak.");
		User user = new User(payload.getUsername(), this.passwordEncoder.encode(payload.getPassword()), this.locationRepository.getById(1L));
		return this.userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public UserData getData(long userId) {
		var user = this.userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return new UserData(user);
	}

	@Transactional(readOnly = true)
	public List<ActionData> getAvailableActions(long userId) {
		var user = this.userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		List<ActionData> actions = new ArrayList<>();
		actions.add(new ActionData("exit", "Exit the game and return to the main menu."));
		actions.add(new ActionData("examine", "Look around and observe your current location."));

		var loc = user.getLocation();
		if (!loc.getAdjacentLocations().isEmpty()) {
			actions.add(new ActionData(
					"goto",
					"Travel to the selected location.",
					List.of(new ActionData.Option(
							"destination",
							"The location you want to travel to.",
							true,
							loc.getAdjacentLocations().stream().sorted()
									.map(l -> new ActionData.Option.Choice(l.getName(), l.getDescription()))
									.toList()
					))
			));
		}

		return actions;
	}

	@Transactional
	public void goOnline(User user) {
		user.setOnline(true);
		this.userRepository.save(user);
	}

	@Transactional
	public void goOffline(User user) {
		user.setOnline(false);
		this.userRepository.save(user);
	}

	@Transactional
	public ActionResponse goTo(User u, String locationName) {
		var user = this.userRepository.findById(u.getId()).orElseThrow();
		var newLocation = this.locationRepository.findByName(locationName)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown location."));
		if (!user.getLocation().getAdjacentLocations().contains(newLocation)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot go to a far-away location.");
		}
		user.setLocation(newLocation);
		return ActionResponse.success("You have travelled to " + newLocation.getName() + ".");
	}
}
