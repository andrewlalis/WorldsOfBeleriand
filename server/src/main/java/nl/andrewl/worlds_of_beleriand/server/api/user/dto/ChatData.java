package nl.andrewl.worlds_of_beleriand.server.api.user.dto;

public record ChatData(
		long id,
		String createdAt,
		String author,
		String message
) {
}
