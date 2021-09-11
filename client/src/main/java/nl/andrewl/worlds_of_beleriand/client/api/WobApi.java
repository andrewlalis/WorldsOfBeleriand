package nl.andrewl.worlds_of_beleriand.client.api;

import com.fasterxml.jackson.databind.JsonNode;
import nl.andrewl.worlds_of_beleriand.client.api.dto.TokenPair;
import nl.andrewl.worlds_of_beleriand.client.api.dto.UsernamePasswordPayload;
import nl.andrewl.worlds_of_beleriand.client.util.JsonUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

public class WobApi {
	private final HttpClient httpClient = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(3))
			.version(HttpClient.Version.HTTP_2)
			.followRedirects(HttpClient.Redirect.ALWAYS)
			.build();
	private final String baseUrl;

	private String accessToken;
	private String refreshToken;
	private Long userId;

	private WobApi(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public static WobApi register(String baseUrl, String username, String password) {
		WobApi api = new WobApi(baseUrl);
		JsonNode result = api.post(
				"/users/register",
				new UsernamePasswordPayload(username, password),
				Map.of(),
				JsonNode.class
		);
		return login(baseUrl, null, username, password);
	}

	public static WobApi login(String baseUrl, String refreshToken, String username, String password) {
		WobApi api = new WobApi(baseUrl);
		if (refreshToken == null) {
			api.fetchNewRefreshToken(username, password);
		} else {
			api.refreshToken = refreshToken;
			api.fetchNewAccessToken();
		}
		return api;
	}

	public void fetchNewRefreshToken(String username, String password) {
		var response = post(
				"/tokens/refresh",
				Map.of("username", username, "password", password),
				Map.of(),
				TokenPair.class
		);
		this.refreshToken = response.refreshToken();
		this.accessToken = response.accessToken();
	}

	public void fetchNewAccessToken() {
		var response = get("/tokens/access", Map.of("Authorization", "Bearer " + this.refreshToken), JsonNode.class);
		this.accessToken = response.get("accessToken").asText();
	}

	public boolean checkAccessToken() {
		JsonNode status = get("/tokens/checkAccess", JsonNode.class);
		boolean valid = status.get("valid").asBoolean();
		long ttl = status.get("ttl").asLong();
		return valid && ttl > 300000;
	}

	private <T> T getResponse(HttpRequest request, Class<T> responseType) throws ApiResponseException {
		try {
			var response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
			if (response.statusCode() >= 200 && response.statusCode() < 400) {
				return JsonUtils.read(response.body(), responseType);
			} else {
				throw new ApiResponseException(response.statusCode(), JsonUtils.read(response.body(), JsonNode.class).get("message").asText());
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			throw new ApiResponseException(-1, e.getMessage());
		}
	}

	public <T> T post(String url, Object body, Map<String, String> headers, Class<T> responseType) {
		var requestBuilder = HttpRequest.newBuilder(URI.create(baseUrl + url))
				.POST(HttpRequest.BodyPublishers.ofString(JsonUtils.writeString(body)))
				.header("Content-Type", "application/json");
		for (var entry : headers.entrySet()) {
			requestBuilder.header(entry.getKey(), entry.getValue());
		}
		return this.getResponse(requestBuilder.build(), responseType);
	}

	public <T> T post(String url, Object body, Class<T> responseType) {
		var headers = Map.of("Authorization", "Bearer " + this.accessToken);
		try {
			return post(url, body, headers, responseType);
		} catch (ApiResponseException e) {
			if (e.getMessage().equals("Expired token.")) {
				this.fetchNewAccessToken();
				return post(url, body, headers, responseType);
			}
			throw e;
		}
	}

	public JsonNode post(String url, Object body) {
		return post(url, body, JsonNode.class);
	}

	public <T> T get(String url, Map<String, String> headers, Class<T> responseType) {
		var requestBuilder = HttpRequest.newBuilder(URI.create(baseUrl + url))
				.GET()
				.header("Accept", "application/json");
		for (var entry : headers.entrySet()) {
			requestBuilder.header(entry.getKey(), entry.getValue());
		}
		return this.getResponse(requestBuilder.build(), responseType);
	}

	public <T> T get(String url, Class<T> responseType) {
		var headers = Map.of("Authorization", "Bearer " + this.accessToken);
		try {
			return get(url, headers, responseType);
		} catch (ApiResponseException e) {
			if (e.getMessage().equals("Expired token.")) {
				this.fetchNewAccessToken();
				return get(url, headers, responseType);
			}
			throw e;
		}
	}

	public JsonNode get(String url) {
		return get(url, JsonNode.class);
	}
}
