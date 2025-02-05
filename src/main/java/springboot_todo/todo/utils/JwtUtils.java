package springboot_todo.todo.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import springboot_todo.todo.exception.CustomException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtUtils {
    private final Key JWT_SECRET_KEY;
    private final long JWT_EXPIRATION_TIME;

    public JwtUtils(
            @Value("${jwt.secret}") String JWT_SECRET_KEY,
            @Value("${jwt.expiration}") long JWT_EXPIRATION_TIME) {
        this.JWT_SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_SECRET_KEY));
        this.JWT_EXPIRATION_TIME = JWT_EXPIRATION_TIME;
    }

    public String generateJwt(UUID sub, String email) {
        return Jwts.builder()
                .setSubject(String.valueOf(sub))
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME))
                .signWith(JWT_SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public Map<String, String> decodeJwt(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                        .setSigningKey(JWT_SECRET_KEY)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
            String userId = claims.getSubject();
            String email = claims.get("email", String.class);
            Map<String, String> userDecodedData = new HashMap<>();
            userDecodedData.put("userId", userId);
            userDecodedData.put("email", email);
            return userDecodedData;
        } catch (ExpiredJwtException e) {
            throw new CustomException("JWT Token has expired", HttpStatus.UNAUTHORIZED);
        } catch (UnsupportedJwtException e) {
            throw new CustomException("Unsupported JWT token format", HttpStatus.UNAUTHORIZED);
        } catch (MalformedJwtException e) {
            throw new CustomException("Invalid JWT Token", HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e) {
            throw new CustomException("JWT token is empty or null", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new CustomException("An unexpected JWT error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean isJwtExpired(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(JWT_SECRET_KEY).build().parseClaimsJwt(token).getBody()
                    .getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            throw new CustomException("Error while checking token expiration", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
