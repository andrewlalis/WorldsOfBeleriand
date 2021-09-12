package nl.andrewl.worlds_of_beleriand.server.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.andrewl.worlds_of_beleriand.server.model.world.Location;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@NoArgsConstructor
@Getter
public class PublicChat extends Chat {
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Location location;

	public PublicChat(User author, String message, Location location) {
		super(author, message);
		this.location = location;
	}
}
