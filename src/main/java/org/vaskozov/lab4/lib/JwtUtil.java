package org.vaskozov.lab4.lib;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

public class JwtUtil {
    private static final byte[] JWT_SECRET = Base64.getDecoder().decode("5d2f6d1f8065fbedbbc0a0ec047704ddc8054c3c42a92fc7b3c2cbc68df2a868");
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(JWT_SECRET);

    public static String createToken(String username, String role) {
        return Jwts
                .builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SECRET_KEY)
                .compact();
    }

    public static Result<String, JwtError> validateToken(String token) {
        try {
            String username = Jwts
                    .parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
            return Result.success(username);
        } catch (ExpiredJwtException e) {
            return Result.error(new JwtError("Token expired", SC_UNAUTHORIZED));
        } catch (Exception e) {
            return Result.error(new JwtError("Invalid token", SC_UNAUTHORIZED));
        }
    }
}