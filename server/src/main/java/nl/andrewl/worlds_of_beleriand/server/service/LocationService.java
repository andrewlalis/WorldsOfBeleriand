package nl.andrewl.worlds_of_beleriand.server.service;

import lombok.RequiredArgsConstructor;
import nl.andrewl.worlds_of_beleriand.server.api.world.dto.LocationData;
import nl.andrewl.worlds_of_beleriand.server.dao.LocationRepository;
import nl.andrewl.worlds_of_beleriand.server.dao.PublicChatRepository;
import nl.andrewl.worlds_of_beleriand.server.model.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {
	private final LocationRepository locationRepository;
	private final PublicChatRepository chatRepository;

	@Transactional(readOnly = true)
	public List<LocationData> getLocations(User user) {
		return List.of();//TODO: implement this!
	}
}
