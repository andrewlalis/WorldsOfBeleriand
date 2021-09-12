package nl.andrewl.worlds_of_beleriand.server.model.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PrivateChat extends Chat {
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private User recipient;

	public PrivateChat(User author, String message, User recipient) {
		super(author, message);
		this.recipient = recipient;
	}
}
