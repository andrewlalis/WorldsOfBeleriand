package nl.andrewl.worlds_of_beleriand.server.config.security;

import nl.andrewl.worlds_of_beleriand.server.model.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class TokenAuthentication implements Authentication {
	private final User user;
	private final String token;

	public TokenAuthentication(User user, String token) {
		this.user = user;
		this.token = token;
	}

	public TokenAuthentication(User user) {
		this(user, null);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public Object getCredentials() {
		return this.token;
	}

	@Override
	public Object getDetails() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return this.user;
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		// Do not allow changing the authentication state.
	}

	@Override
	public String getName() {
		return this.user.getName();
	}
}
