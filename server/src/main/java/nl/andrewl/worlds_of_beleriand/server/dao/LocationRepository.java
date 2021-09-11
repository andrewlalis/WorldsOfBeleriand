package nl.andrewl.worlds_of_beleriand.server.dao;

import nl.andrewl.worlds_of_beleriand.server.model.world.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
