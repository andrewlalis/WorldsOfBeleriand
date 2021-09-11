package nl.andrewl.worlds_of_beleriand.client;

import nl.andrewl.worlds_of_beleriand.client.api.ApiResponseException;
import nl.andrewl.worlds_of_beleriand.client.api.WobApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client {

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
		System.out.println(api.checkAccessToken());
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
