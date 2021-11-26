package com.craig.pe.demosecuritywebflux.handler;

import com.craig.pe.demosecuritywebflux.documents.Role;
import com.craig.pe.demosecuritywebflux.documents.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


import static com.craig.pe.demosecuritywebflux.utils.Constants.*;

@Component
public class TokenProvider implements Serializable {

    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
        final Claims claims= getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token){
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenExpired(String token){
        final Date expiration=getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(User user){
        final List<String> authorities=user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim(AUTHORITIES_KEY , authorities)
                .signWith(SignatureAlgorithm.ES256, SIGNING_KEY)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
                .compact();
    }
}
