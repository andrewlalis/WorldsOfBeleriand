package nl.andrewl.worlds_of_beleriand.server.api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Simple container for a pair of access and refresh token.
 */
@AllArgsConstructor
@Getter
public class TokenPair {
	private final String accessToken;
	private final String refreshToken;
}
