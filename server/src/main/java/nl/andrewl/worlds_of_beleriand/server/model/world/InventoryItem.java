package nl.andrewl.worlds_of_beleriand.server.model.world;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class InventoryItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Item item;

	@Column(nullable = false)
	private int amount;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof InventoryItem other)) return false;
		if (this.id != null && other.getId() != null) return this.id.equals(other.getId());
		return this.item.equals(other.getItem()) && this.amount == other.getAmount();
	}
}
