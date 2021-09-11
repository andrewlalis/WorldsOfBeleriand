package nl.andrewl.worlds_of_beleriand.server.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.andrewl.worlds_of_beleriand.server.dao.UserRepository;
import nl.andrewl.worlds_of_beleriand.server.service.TokenService;
import nl.andrewl.worlds_of_beleriand.server.util.JsonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
	private final UserRepository userRepository;
	private final TokenService tokenService;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
		try {
			String token = this.tokenService.extractToken(request);
			Jws<Claims> jwt = this.tokenService.getToken(token, this.getRequiredTokenType(request));
			if (jwt != null) {
				String username = jwt.getBody().get("username", String.class);
				var optionalUser = this.userRepository.findByName(username);
				optionalUser.ifPresent(user -> SecurityContextHolder.getContext().setAuthentication(new TokenAuthentication(optionalUser.get(), token)));
			}
		} catch (ExpiredJwtException e) {
			JsonUtils.write(response, HttpStatus.UNAUTHORIZED.value(), Map.of("message", "Expired token."));
			return;
		} catch (SignatureException | UnsupportedJwtException e) {
			JsonUtils.write(response, HttpStatus.UNAUTHORIZED.value(), Map.of("message", "Invalid token."));
			return;
		} catch (MalformedJwtException e) {
			JsonUtils.write(response, HttpStatus.UNAUTHORIZED.value(), Map.of("message", "Malformed token."));
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		filterChain.doFilter(request, response);
	}

	/**
	 * Determines the token type required for a request. This will be either
	 * "refresh" or "access". The only time a refresh token is required is for
	 * GET requests to the /api/tokens endpoint for requesting a new access token.
	 * @param request The request to get the required token type for.
	 * @return The token type that is required.
	 */
	private String getRequiredTokenType(HttpServletRequest request) {
		String tokenType;
		if (request.getRequestURI().contains("/api/tokens/access") && request.getMethod().equalsIgnoreCase("get")) {
			tokenType = "refresh";
		} else {
			tokenType = "access";
		}
		return tokenType;
	}
}
