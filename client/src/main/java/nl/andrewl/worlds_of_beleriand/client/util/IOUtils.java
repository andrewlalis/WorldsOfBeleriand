package nl.andrewl.worlds_of_beleriand.client.util;

import nl.andrewl.worlds_of_beleriand.client.api.dto.ActionData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Function;

public class IOUtils {

	public static String input(Function<String, Boolean> validator) throws IOException {
		var reader = new BufferedReader(new InputStreamReader(System.in));
		do {
			String input = reader.readLine();
			if (validator.apply(input)) return input;
			System.out.println("Invalid input.");
		} while (true);
	}

	public static String selectChoice(List<ActionData.Option.Choice> choices, boolean required) throws IOException {
		System.out.println("Please select one of the following choices:");
		for (int i = 0; i < choices.size(); i++) {
			var choice = choices.get(i);
			System.out.printf("[%d] %s - %s\n", i + 1, choice.name(), choice.description());
		}
		if (!required) {
			System.out.println("[-1] Don't pick any choice.");
		}
		var reader = new BufferedReader(new InputStreamReader(System.in));
		do {
			String input = reader.readLine();
			try {
				int choiceIndex = Integer.parseInt(input);
				if (choiceIndex == -1 && !required) {
					return null;
				} else if (choiceIndex > 0 && choiceIndex <= choices.size()) {
					return choices.get(choiceIndex - 1).name();
				}
			} catch (NumberFormatException ignored) {
				System.out.println("Invalid input.");
			}
		} while (true);
	}
}
