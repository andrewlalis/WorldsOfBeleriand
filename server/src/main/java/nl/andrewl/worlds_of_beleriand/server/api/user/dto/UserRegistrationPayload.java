package nl.andrewl.worlds_of_beleriand.server.api.user.dto;

import lombok.Data;

@Data
public class UserRegistrationPayload {
	private String username;
	private String password;
}
