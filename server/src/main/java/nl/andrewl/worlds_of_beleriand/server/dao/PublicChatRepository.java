package nl.andrewl.worlds_of_beleriand.server.dao;

import nl.andrewl.worlds_of_beleriand.server.model.user.PublicChat;
import nl.andrewl.worlds_of_beleriand.server.model.world.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicChatRepository extends JpaRepository<PublicChat, Long> {
	Page<PublicChat> findAllByLocationOrderByCreatedAtDesc(Location location, Pageable pageable);
}
