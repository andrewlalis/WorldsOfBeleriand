package nl.andrewl.worlds_of_beleriand.server.api.user.dto;

import lombok.Getter;
import nl.andrewl.worlds_of_beleriand.server.model.user.User;

import java.time.format.DateTimeFormatter;

@Getter
public class UserData {
	private Long id;
	private String username;
	private String createdAt;

	public UserData(User user) {
		this.id = user.getId();
		this.username = user.getName();
		this.createdAt = user.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}
}
