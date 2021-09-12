package nl.andrewl.worlds_of_beleriand.server.model.world;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@Getter
public class Item implements Comparable<Item> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	@Column(nullable = false, length = 4096)
	private String description;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Item item)) return false;
		return Objects.equals(getId(), item.getId()) && getName().equals(item.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getName());
	}

	@Override
	public int compareTo(Item o) {
		return this.getName().compareTo(o.getName());
	}
}
