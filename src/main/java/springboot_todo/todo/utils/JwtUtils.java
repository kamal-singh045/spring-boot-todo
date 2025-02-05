package springboot_todo.todo.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

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

    public String generateJwt(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME))
                .signWith(JWT_SECRET_KEY)
                .compact();
    }

    public String decodeJwt(String token) {
        return Jwts.parserBuilder().setSigningKey(JWT_SECRET_KEY).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isJwtExpired(String token) {
        return Jwts.parserBuilder().setSigningKey(JWT_SECRET_KEY).build().parseClaimsJwt(token).getBody().getExpiration().before(new Date());
    }
}
