package com.bwesound.identity.auth.token;

import com.bwesound.identity.config.JwtProperties;
import com.bwesound.identity.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;
    private final SecretKey signingKey;

    public JwtService(JwtProperties jwtProperties){
        this.jwtProperties = jwtProperties;
        this.signingKey = createSigningKey(jwtProperties.getSecret());
    }

    public AccessToken issueAccessToken(User user){
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(jwtProperties.getAccessTokenTtl());

        String token = Jwts.builder()
                .issuer("bwe-sound-identity")
                .subject(user.getId().toString())
                .id(UUID.randomUUID().toString())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .claim("email",user.getEmail())
                .claim("roles", List.of(user.getRole().name()))
                .signWith(signingKey)
                .compact();
        return new AccessToken(token,expiresAt);
    }

    private SecretKey createSigningKey(String secret){
        if(secret == null || secret.isBlank()){
            throw new IllegalStateException("JWT_SECRET must be configured");
        }
        try {
            byte[] secretBytes = Decoders.BASE64.decode(secret);
            return Keys.hmacShaKeyFor(secretBytes);
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException(
                    "JWT_SECRET must be a valid Base64 value",
                    exception

            );
        }
    }
}
