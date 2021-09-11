package nl.andrewl.worlds_of_beleriand.server.api.user;

import lombok.RequiredArgsConstructor;
import nl.andrewl.worlds_of_beleriand.server.api.user.dto.TokenPair;
import nl.andrewl.worlds_of_beleriand.server.api.user.dto.UsernamePasswordTokenRequest;
import nl.andrewl.worlds_of_beleriand.server.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(path = "/tokens")
@RequiredArgsConstructor
public class TokenController {
	private final TokenService tokenService;

	@GetMapping("/access")
	public Map<String, String> getAccessToken(HttpServletRequest request) {
		String token = this.tokenService.extractToken(request);
		if (token == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing authorization bearer token.");
		String accessToken = this.tokenService.generateAccessToken(token);
		return Map.of("accessToken", accessToken);
	}

	@GetMapping("/checkAccess")
	public Map<String, Object> checkAccess(HttpServletRequest request) {
		boolean valid = false;
		Long timeToLive = null;
		try {
			var token = this.tokenService.getToken(this.tokenService.extractToken(request), "access");
			timeToLive = token.getBody().getExpiration().toInstant().toEpochMilli() - System.currentTimeMillis();
			valid = token != null;
		} catch (Exception ignored) {}
		return Map.of("valid", valid, "ttl", timeToLive);
	}

	@PostMapping("/refresh")
	public TokenPair createTokenPair(@Valid @RequestBody UsernamePasswordTokenRequest tokenRequest) {
		return this.tokenService.generateTokenPair(tokenRequest);
	}
}
