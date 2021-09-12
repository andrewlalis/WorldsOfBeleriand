package nl.andrewl.worlds_of_beleriand.server.api.world;

import lombok.RequiredArgsConstructor;
import nl.andrewl.worlds_of_beleriand.server.api.world.dto.LocationData;
import nl.andrewl.worlds_of_beleriand.server.model.user.User;
import nl.andrewl.worlds_of_beleriand.server.service.LocationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/world/locations")
@RequiredArgsConstructor
public class LocationsController {
	private final LocationService locationService;

	@GetMapping
	public List<LocationData> getLocations(User user) {
		return this.locationService.getLocations(user);
	}
}
