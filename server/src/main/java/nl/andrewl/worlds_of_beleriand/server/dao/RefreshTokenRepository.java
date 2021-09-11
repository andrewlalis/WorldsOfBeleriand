package nl.andrewl.worlds_of_beleriand.server.dao;

import nl.andrewl.worlds_of_beleriand.server.model.user.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Iterable<RefreshToken> findAllBySuffix(String suffix);

	@Modifying
	void deleteAllByExpiresAtBefore(LocalDateTime time);
}
