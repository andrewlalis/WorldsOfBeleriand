package nl.andrewl.worlds_of_beleriand.server.api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The data that is received when a client requests a token, and provides their
 * credentials.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsernamePasswordTokenRequest {
	private String username;
	private String password;
}
