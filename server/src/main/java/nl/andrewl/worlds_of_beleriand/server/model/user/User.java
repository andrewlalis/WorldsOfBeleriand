package nl.andrewl.worlds_of_beleriand.server.model.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.andrewl.worlds_of_beleriand.server.model.world.Location;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 63)
	private String name;

	@CreationTimestamp
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private String passwordHash;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Location location;

	public User(String name, String passwordHash, Location location) {
		this.name = name;
		this.passwordHash = passwordHash;
		this.location = location;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof User user)) return false;
		if (this.getId() == null || user.getId() == null) return this.getName().equals(user.getName());
		return this.getId().equals(user.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getName());
	}
}
