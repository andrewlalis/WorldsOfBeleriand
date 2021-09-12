package nl.andrewl.worlds_of_beleriand.server.model.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class Chat implements Comparable<Chat> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreationTimestamp
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdAt;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private User author;

	@Column(nullable = false, updatable = false, length = 2048)
	private String message;

	public Chat(User author, String message) {
		this.author = author;
		this.message = message;
	}

	@Override
	public int compareTo(Chat o) {
		if (this.createdAt != null && o.getCreatedAt() != null) {
			return this.createdAt.compareTo(o.getCreatedAt());
		}
		return this.message.compareTo(o.getMessage());
	}
}
