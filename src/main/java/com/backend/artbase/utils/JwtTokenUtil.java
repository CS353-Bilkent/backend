package com.backend.artbase.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.backend.artbase.entities.User;

import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtil {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.ttl}")
    private long ttl;

    @Value("${jwt.issuer}")
    private String issuer;

    public String generateAccessToken(User user) {
        return Jwts.builder().setSubject(user.getUserId().toString()).setIssuer(issuer).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ttl)).signWith(SignatureAlgorithm.HS512, secretKey).compact();

    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.debug("Expired JWT token: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.debug("JWT Token is null, empty or only whitespace " + ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.debug("JWT is invalid " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.debug("JWT is not supported " + ex.getMessage());
        } catch (SignatureException ex) {
            log.debug("Signature validation failed " + ex.getMessage());
        }

        return false;
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

}
