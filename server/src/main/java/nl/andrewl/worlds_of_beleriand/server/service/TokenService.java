package nl.andrewl.worlds_of_beleriand.server.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.andrewl.worlds_of_beleriand.server.api.user.dto.TokenPair;
import nl.andrewl.worlds_of_beleriand.server.api.user.dto.UsernamePasswordTokenRequest;
import nl.andrewl.worlds_of_beleriand.server.config.security.TokenAuthentication;
import nl.andrewl.worlds_of_beleriand.server.dao.RefreshTokenRepository;
import nl.andrewl.worlds_of_beleriand.server.dao.UserRepository;
import nl.andrewl.worlds_of_beleriand.server.model.user.RefreshToken;
import nl.andrewl.worlds_of_beleriand.server.model.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * This services handles the process of generating and verifying access and
 * refresh tokens for users.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
	private static final String BEARER_PREFIX = "Bearer ";
	private static PrivateKey privateKey = null;
	public static final String ISSUER = "DOPP";

	private final PasswordEncoder encoder;
	private final RefreshTokenRepository refreshTokenRepository;
	private final UserRepository userRepository;

	/**
	 * The location of the private key (in DER format) that will be used to sign
	 * and decrypt JWS tokens. This should be specified in an external config
	 * not tracked by the VCS.
	 */
	@Value("${private-key-location}")
	private String privateKeyLocation;

	/**
	 * Generates a new pair of a short-lived access token, and a long-lived
	 * refresh token.
	 * @param auth The authentication to generate the tokens for.
	 * @return A token pair.
	 */
	@Transactional
	public TokenPair generateTokenPair(Authentication auth) {
		String refreshToken = this.generateRefreshToken(auth);
		return new TokenPair(this.generateAccessToken(refreshToken), refreshToken);
	}

	/**
	 * Generates a new pair of access and refresh tokens for a username and
	 * password combination.
	 * @param request The username and password request.
	 * @return A token pair.
	 */
	@Transactional
	public TokenPair generateTokenPair(UsernamePasswordTokenRequest request) {
		var optionalUser = this.userRepository.findByName(request.getUsername());
		if (
			optionalUser.isEmpty()
			|| !this.encoder.matches(request.getPassword(), optionalUser.get().getPasswordHash())
		) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials.");
		User user = optionalUser.get();
		return this.generateTokenPair(new TokenAuthentication(user));
	}

	/**
	 * Generates a new access token from a refresh token.
	 * @param refreshTokenString The refresh token that the client must provide
	 *                           to generate this access token.
	 * @return The newly-generated token.
	 */
	@Transactional(readOnly = true)
	public String generateAccessToken(String refreshTokenString) {
		String suffix = refreshTokenString.substring(refreshTokenString.length() - RefreshToken.SUFFIX_LENGTH);
		// Try and find a matching refresh token.
		RefreshToken refreshToken = null;
		for (var rt : this.refreshTokenRepository.findAllBySuffix(suffix)) {
			if (this.encoder.matches(refreshTokenString, rt.getHash())) {
				refreshToken = rt;
				break;
			}
		}
		// Throw an unauthorized exception if the token is invalid in any way.
		if (refreshToken == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unknown refresh token.");
		if (refreshToken.isDisabled()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is disabled.");
		if (refreshToken.isExpired()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is expired.");
		// Finally, build the access token.
		Instant expiration = OffsetDateTime.now(ZoneOffset.UTC).plusHours(1).toInstant();
		try {
			return Jwts.builder()
					.setSubject(Long.toHexString(refreshToken.getUser().getId()))
					.setIssuer(ISSUER)
					.setAudience("Muxsan Client Application")
					.setExpiration(Date.from(expiration))
					.claim("username", refreshToken.getUser().getName())
					.claim("token_type", "access")
					.signWith(this.getPrivateKey())
					.compact();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not issue a token.", e);
		}
	}

	/**
	 * Generates a new refresh token for an authenticated user.
	 * @param auth The authenticated user.
	 * @return The refresh token that was generated.
	 */
	@Transactional
	protected String generateRefreshToken(Authentication auth) {
		User user = (User) auth.getPrincipal();
		Instant expiration = OffsetDateTime.now(ZoneOffset.UTC).plusDays(7).toInstant();
		try {
			String token = Jwts.builder()
					.setSubject(Long.toHexString(user.getId()))
					.setIssuer(ISSUER)
					.setAudience("Muxsan Client Application")
					.setExpiration(Date.from(expiration))
					.claim("username", user.getName())
					.claim("token_type", "refresh")
					.signWith(this.getPrivateKey())
					.compact();
			this.refreshTokenRepository.saveAndFlush(new RefreshToken(
					this.encoder.encode(token),
					token.substring(token.length() - RefreshToken.SUFFIX_LENGTH),
					user,
					LocalDateTime.ofInstant(expiration, ZoneOffset.UTC)
			));
			return token;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not generate a refresh token.", e);
		}
	}

	public String extractToken(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) return null;
		return authorizationHeader.substring(BEARER_PREFIX.length());
	}

	/**
	 * Extracts a signed JWT from a raw token.
	 * @param token The token to extract.
	 * @param tokenType The type of token which we require.
	 * @return The JWT that was obtained, or null if none was found.
	 * @throws Exception If an error occurred in obtaining a private signing key
	 * or in extracting the JWT.
	 */
	public Jws<Claims> getToken(String token, String tokenType) throws Exception {
		if (token == null) return null;
		var builder = Jwts.parserBuilder()
				.setSigningKey(this.getPrivateKey())
				.requireIssuer(ISSUER);
		if (tokenType != null) {
			builder.require("token_type", tokenType);
		}
		return builder.build().parseClaimsJws(token);
	}

	/**
	 * Scheduled task to automatically remove expired tokens.
	 */
	@Scheduled(fixedRateString = "${refresh-token-removal-interval}")
	public void pruneExpiredRefreshTokens() {
		this.refreshTokenRepository.deleteAllByExpiresAtBefore(LocalDateTime.now());
	}

	/**
	 * @return The private key used to sign the JWT. It is lazily-loaded from a
	 * file in working directory of this application.
	 */
	private PrivateKey getPrivateKey() throws Exception {
		if (privateKey == null) {
			byte[] keyBytes = Files.readAllBytes(Path.of(this.privateKeyLocation));
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			privateKey = kf.generatePrivate(spec);
		}
		return privateKey;
	}
}
