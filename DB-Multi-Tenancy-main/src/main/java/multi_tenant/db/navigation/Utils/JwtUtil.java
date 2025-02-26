package multi_tenant.db.navigation.Utils;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;


@Component
public class JwtUtil {

	private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

	private SecretKey getSigningKey1() {
//		System.out.println("Using key: " + Base64.getEncoder().encodeToString(SECRET_KEY.getEncoded()));
		return SECRET_KEY;
	}
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		System.out.println("I a in extractclaim");
		final Claims claims = extractAllClaims(token);
		System.out.println("done extract all claims");
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		System.out.println("I am in extract all claims");
		try {
			Claims claims = Jwts.parser().verifyWith(getSigningKey1()).build().parseSignedClaims(token).getPayload();
			System.out.println("printing the result: " + claims);
			return claims;
		} catch (Exception e) {
			System.out.println("Error parsing JWT: " + e.getMessage());
			e.printStackTrace();
			throw e; // Re-throw the exception or handle it as needed
		}
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	// most important: taking userDetails into jwt
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails.getUsername());
	}

	private String createToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().claims(claims).subject(subject).issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)).signWith(getSigningKey1())
				.compact();

	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);

		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
