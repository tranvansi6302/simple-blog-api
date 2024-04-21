package com.simpleblogapi.simpleblogapi.components;

import com.simpleblogapi.simpleblogapi.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(User user) {
        // properties => claims
        Map<String, Object> claims = Map.of(
                "email", user.getEmail(),
                "fullname", user.getFullName(),
                "role", user.getRole().getRoleName()
        );
        try {
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getEmail())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .compact();
            return token;

        } catch (Exception e) {
            System.out.println("Can not generate token, error: " + e.getMessage());
            return null;
        }
    }

    private Claims extractAllClaims(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.out.println("Can not extract claims, error: " + e.getMessage());
            return null;
        }
    }
    // Extract one claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // check expiration
    public Boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    public String getEmailFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public Long getIdFromToken(String token) {
        return Long.parseLong(extractClaim(token, Claims::getId));
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String email = getEmailFromToken(token);
        return (email.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
}
