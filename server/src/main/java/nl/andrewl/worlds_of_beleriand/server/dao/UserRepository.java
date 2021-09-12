package nl.andrewl.worlds_of_beleriand.server.dao;

import nl.andrewl.worlds_of_beleriand.server.model.user.User;
import nl.andrewl.worlds_of_beleriand.server.model.world.Location;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	boolean existsByName(String name);
	Optional<User> findByName(String name);
	List<User> findAllByLocationAndOnlineIsTrue(Location location);
}
