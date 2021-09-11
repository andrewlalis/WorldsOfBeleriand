package nl.andrewl.worlds_of_beleriand.server.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

@Configuration
public class SecurityComponents {
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(8, new SecureRandom());
	}
}
