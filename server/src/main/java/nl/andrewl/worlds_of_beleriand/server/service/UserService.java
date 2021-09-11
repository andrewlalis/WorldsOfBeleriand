package nl.andrewl.worlds_of_beleriand.server.service;

import lombok.RequiredArgsConstructor;
import nl.andrewl.worlds_of_beleriand.server.api.user.dto.UserData;
import nl.andrewl.worlds_of_beleriand.server.api.user.dto.UserRegistrationPayload;
import nl.andrewl.worlds_of_beleriand.server.dao.LocationRepository;
import nl.andrewl.worlds_of_beleriand.server.dao.UserRepository;
import nl.andrewl.worlds_of_beleriand.server.model.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
}
