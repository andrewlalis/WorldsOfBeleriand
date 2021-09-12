package nl.andrewl.worlds_of_beleriand.server.api.world.action.dto;

import java.util.Collections;
import java.util.List;

public record ActionData(String name, String description, List<Option> options) {
	public ActionData(String name, String description) {
		this(name, description, Collections.emptyList());
	}

	public static record Option(
			String name,
			String description,
			boolean required,
			List<Choice> choices
	) {
		public Option(String name, String description, boolean required) {
			this(name, description, required, null);
		}

		public Option(String name, String description) {
			this(name, description, false);
		}

		public static record Choice(String name, String description) {
			public Choice(String name) {
				this(name, null);
			}
		}
	}
}
