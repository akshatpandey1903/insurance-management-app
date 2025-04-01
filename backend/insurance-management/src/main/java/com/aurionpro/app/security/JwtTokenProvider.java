package com.aurionpro.app.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.aurionpro.app.exceptions.UserApiException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
	@Value("${app.jwt-secret}")
	private String jwtSecret;
	
	@Value("${app.jwt-expiration-milliseconds}")
	private long jwtExpirationDate;
	
	public String generateToken(Authentication authentication) {
		String username = authentication.getName();
		Date currentDate = new Date();
		Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
		
		String token = Jwts.builder().claims().subject(username)
				.issuedAt(new Date(System.currentTimeMillis())).expiration(expireDate)
				.and().signWith(key())
				.claim("role", authentication.getAuthorities())
				.compact();
		return token;
	}
	
	private SecretKey key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parser().verifyWith(key()).build().parse(token);
			return true;
		}catch(MalformedJwtException e) {
			throw new UserApiException(HttpStatus.BAD_REQUEST, "Invalid Jwt Token");
		}catch(IllegalArgumentException e) {
			throw new UserApiException(HttpStatus.BAD_REQUEST, "JWT claims string is empty");
		}catch(ExpiredJwtException e) {
			throw new UserApiException(HttpStatus.BAD_REQUEST, "Expired Jwt Token");
		}catch(UnsupportedJwtException e) {
			throw new UserApiException(HttpStatus.BAD_REQUEST, "Unsupported Jwt Token");
		}
		catch(Exception e) {
			throw new UserApiException(HttpStatus.BAD_REQUEST, "Invalid credentials");
		}
	}
	
	public String getUsername(String token) {
		Claims claims = Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload();
		String username = claims.getSubject();
		return username;
	}
}