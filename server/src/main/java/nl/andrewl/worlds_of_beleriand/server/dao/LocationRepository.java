package nl.andrewl.worlds_of_beleriand.server.dao;

import nl.andrewl.worlds_of_beleriand.server.model.user.User;
import nl.andrewl.worlds_of_beleriand.server.model.world.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
	Optional<Location> findByUsersContaining(User user);

	Optional<Location> findByName(String name);
}
