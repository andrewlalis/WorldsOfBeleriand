package nl.andrewl.worlds_of_beleriand.server.api.world.action.dto;

public record ActionResponse(Status status, String message) {
	public enum Status {SUCCESS, WARNING, ERROR}

	public static ActionResponse success(String message) {
		return new ActionResponse(Status.SUCCESS, message);
	}

	public static ActionResponse warning(String message) {
		return new ActionResponse(Status.WARNING, message);
	}

	public static ActionResponse error(String message) {
		return new ActionResponse(Status.ERROR, message);
	}
}
