package nl.andrewl.worlds_of_beleriand.server.model.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * A refresh token is a long-lived token that represents a client's status as
 * authenticated successfully. The refresh token is used to periodically obtain
 * new access tokens; these access tokens are used to access the API.
 */
@Entity
@Table(name = "user_refresh_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefreshToken {
	public static final int SUFFIX_LENGTH = 7;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	/**
	 * A salted, hashed version of the original token, used to check the
	 * validity of refresh tokens without storing the plaintext token.
	 */
	private String hash;

	/**
	 * A short suffix of the token which is used to greatly speed up the process
	 * of finding a token whose hash matches a given plaintext token, without
	 * needing to store the full token in plaintext.
	 */
	@Column(nullable = false, updatable = false)
	private String suffix;

	/**
	 * The user that this token belongs to.
	 */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private User user;

	/**
	 * The date and time (in UTC) when this token was created.
	 */
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	/**
	 * The date and time (in UTC) when this token expires, after which it is no
	 * longer possible to obtain new access tokens, and the client will have to
	 * re-authenticate.
	 */
	@Column(nullable = false, updatable = false)
	private LocalDateTime expiresAt;

	/**
	 * Whether this refresh token is disabled. Only non-disabled tokens may be
	 * used to obtain access tokens. This is mostly useful for locking a user
	 * out of the system and preventing them from logging back in.
	 */
	@Column(nullable = false)
	@Setter
	private boolean disabled = false;

	public RefreshToken(String hash, String suffix, User user, LocalDateTime expiresAt) {
		this.hash = hash;
		this.suffix = suffix;
		this.user = user;
		this.expiresAt = expiresAt;
	}

	/**
	 * Convenience method to check if this token is expired, using the current
	 * system time.
	 * @return True if this token is expired, or false if not.
	 */
	public boolean isExpired() {
		return this.expiresAt.isBefore(LocalDateTime.now(ZoneOffset.UTC));
	}
}
