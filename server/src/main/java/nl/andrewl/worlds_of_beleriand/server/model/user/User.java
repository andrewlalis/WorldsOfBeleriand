package nl.andrewl.worlds_of_beleriand.server.model.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.andrewl.worlds_of_beleriand.server.model.world.InventoryItem;
import nl.andrewl.worlds_of_beleriand.server.model.world.Location;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

	@Column(nullable = false)
	@Setter
	private boolean online;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@Setter
	private Location location;

	@OneToMany(orphanRemoval = true)
	private Set<InventoryItem> inventoryItems;

	public User(String name, String passwordHash, Location location) {
		this.name = name;
		this.passwordHash = passwordHash;
		this.location = location;
		this.online = true;
		this.inventoryItems = new HashSet<>();
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
