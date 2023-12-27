package com.electronicstore.security;

import com.electronicstore.constants.AppConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Component
public class JwtHelper {
    //validate
    //generate krto
    // Get UserName from Token


    private String secret = "jwtTokenKey";

    // retrieve username from jwtToken
    public String getUsernamefromToken(String token) {

        return getClaimFromToken(token, Claims::getSubject);

    }

    // retrieve Expiration date from token
    public Date getExpirationDateFromToken(String token) {

        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {

        final Claims claims = getAllClaimsfromToken(token);

        return claimResolver.apply(claims);

    }

    public Claims getAllClaimsfromToken(String token) {

        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    // check if token is expired
    public Boolean isTokenExpired(String token) {

        final Date expiration = getExpirationDateFromToken(token);

        return expiration.before(new Date());
    }

    // generate token for user

    public String generateToken(UserDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();

        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + AppConstant.JWT_TOKEN_VALIDITY * 100))
                .signWith(SignatureAlgorithm.HS512, secret).compact();

    }

    public Boolean validateToken(String token, UserDetails userDetails) {

        final String username = getUsernamefromToken(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

    }
}
