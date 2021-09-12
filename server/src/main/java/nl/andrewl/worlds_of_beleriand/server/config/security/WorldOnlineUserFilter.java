package nl.andrewl.worlds_of_beleriand.server.config.security;

import nl.andrewl.worlds_of_beleriand.server.model.user.User;
import nl.andrewl.worlds_of_beleriand.server.util.JsonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * This filter is applied to ensure that users accessing the world API have been
 * set to "online". Offline users cannot access any world endpoints.
 */
@Component
public class WorldOnlineUserFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = auth == null ? null : (User) auth.getPrincipal();
		if (user == null) {
			JsonUtils.write(response, HttpStatus.FORBIDDEN.value(), Map.of("message", "Cannot access World API without authentication."));
		} else if (!user.isOnline()) {
			JsonUtils.write(response, HttpStatus.FORBIDDEN.value(), Map.of("message", "User is offline."));
		} else {
			filterChain.doFilter(request, response);
		}
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return !request.getRequestURI().contains("/world");
	}
}
