package nl.andrewl.worlds_of_beleriand.server.model.world;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.andrewl.worlds_of_beleriand.server.model.user.PublicChat;
import nl.andrewl.worlds_of_beleriand.server.model.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Location implements Comparable<Location> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	@Column(nullable = false, length = 4096)
	private String description;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "location")
	private Set<User> users;

	@ManyToMany(mappedBy = "locations")
	private Set<Pathway> pathways;

	@OneToMany(mappedBy = "location", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@OrderBy("createdAt")
	private List<PublicChat> chats;

	public Location(String name, String description) {
		this.name = name;
		this.description = description;
		this.users = new HashSet<>();
		this.pathways = new HashSet<>();
		this.chats = new ArrayList<>();
	}

	public Set<Location> getAdjacentLocations() {
		Set<Location> locations = new HashSet<>();
		for (var p : this.pathways) {
			for (var l : p.getLocations()) {
				if (!l.equals(this)) locations.add(l);
			}
		}
		return locations;
	}

	@Override
	public int compareTo(Location o) {
		return this.name.compareTo(o.getName());
	}
}
