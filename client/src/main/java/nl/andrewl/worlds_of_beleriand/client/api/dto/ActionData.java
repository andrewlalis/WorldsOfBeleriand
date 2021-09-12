package nl.andrewl.worlds_of_beleriand.client.api.dto;

import java.util.List;

public record ActionData(String name, String description, List<Option> options) {

	public static record Option(String name, String description, boolean required, List<Choice> choices) {

		public boolean hasChoices() {
			return this.choices != null && !this.choices.isEmpty();
		}

		public static record Choice(String name, String description) {}
	}
}
