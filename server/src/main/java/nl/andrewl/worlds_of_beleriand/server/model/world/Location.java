package nl.andrewl.worlds_of_beleriand.server.model.world;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.andrewl.worlds_of_beleriand.server.model.user.User;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, length = 4096)
	private String description;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "location")
	private Set<User> users;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable
	private Set<Location> adjacentLocations;

	public Location(String name, String description) {
		this.name = name;
		this.description = description;
	}
}
