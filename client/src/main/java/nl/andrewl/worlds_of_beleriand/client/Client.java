package nl.andrewl.worlds_of_beleriand.client;

import nl.andrewl.worlds_of_beleriand.client.api.ApiResponseException;
import nl.andrewl.worlds_of_beleriand.client.api.WobApi;
import nl.andrewl.worlds_of_beleriand.client.api.dto.ActionData;
import nl.andrewl.worlds_of_beleriand.client.util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Client {
	private static final Path REFRESH_TOKEN_FILE = Path.of("./.wob_client_token.txt");

	public static void main(String[] args) throws IOException {
		System.out.println("Welcome to Worlds of Beleriand.");
		System.out.println("Type 'register' to register a new account, or simply press enter to login. Type 'exit' to quit.");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String input = reader.readLine();
		WobApi api = null;
		if (input.equalsIgnoreCase("register")) {
			api = register(reader);
		} else if (input.equalsIgnoreCase("exit")) {
			System.exit(0);
		} else {
			api = login(reader);
		}
		boolean shouldExit = false;
		while (!shouldExit) {
			var actions = api.get("/world/actions", ActionData[].class);
			System.out.println("Please choose from one of the following actions:");
			Map<String, ActionData> actionsMap = new HashMap<>();
			for (var action : actions) {
				actionsMap.put(action.name(), action);
				System.out.println(action.name() + " -> " + action.description());
			}
			String chosenActionName = IOUtils.input(actionsMap::containsKey);
			if (chosenActionName.equals("exit")) {
				shouldExit = true;
			}
			ActionData chosenAction = actionsMap.get(chosenActionName);
			Map<String, String> options = new HashMap<>();
			for (var option : chosenAction.options()) {
				System.out.println("Please enter a value for the option " + option.name() + " - " + option.description());
				String value;
				if (option.hasChoices()) {
					value = IOUtils.selectChoice(option.choices(), option.required());
				} else {
					value = IOUtils.input(s -> (!option.required() || (s != null && !s.isBlank())));
				}
				options.put(option.name(), value);
			}
			var actionResponse = api.post("/world/actions/" + chosenActionName, options);
			System.out.println(actionResponse.get("message").asText());
		}
	}

	private static WobApi register(BufferedReader reader) throws IOException {
		do {
			System.out.println("Enter a username:");
			String username = reader.readLine();
			System.out.println("Enter a password:");
			String password = reader.readLine();
			try {
				return WobApi.register("http://localhost:8080/api", username, password);
			} catch (ApiResponseException e) {
				System.out.println("Could not register: " + e.getMessage());
			}
		} while (true);
	}

	private static WobApi login(BufferedReader reader) throws IOException {
		if (Files.exists(REFRESH_TOKEN_FILE)) {
			System.out.println("Loading session from token!");
			String token = Files.readString(REFRESH_TOKEN_FILE);
			return WobApi.loginViaRefreshToken("http://localhost:8080/api", token);
		}
		do {
			System.out.println("Enter a username:");
			String username = reader.readLine();
			System.out.println("Enter a password:");
			String password = reader.readLine();
			try {
				return WobApi.login("http://localhost:8080/api", null, username, password);
			} catch (ApiResponseException e) {
				System.out.println("Could not register: " + e.getMessage());
			}
		} while (true);
	}
}
