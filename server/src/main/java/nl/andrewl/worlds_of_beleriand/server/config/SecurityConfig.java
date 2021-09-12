package nl.andrewl.worlds_of_beleriand.server.config;

import lombok.RequiredArgsConstructor;
import nl.andrewl.worlds_of_beleriand.server.config.security.JwtAuthorizationFilter;
import nl.andrewl.worlds_of_beleriand.server.config.security.WorldOnlineUserFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final JwtAuthorizationFilter jwtAuthorizationFilter;
	private final WorldOnlineUserFilter worldOnlineUserFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.csrf().disable();

		http.authorizeRequests()
				// Allow unauthenticated users to access token endpoints so that they can become authenticated.
				.antMatchers("/api/tokens/**", "/api/users/register").permitAll()
				// All other endpoints require the user to provide a token.
				.anyRequest().authenticated();

		// Tokens are checked with the JWT authorization filter.
		http.addFilterBefore(this.jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

		http.addFilterAfter(this.worldOnlineUserFilter, JwtAuthorizationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Don't configure the builder at all, we are using our own JWT filter and token service.
	}
}
