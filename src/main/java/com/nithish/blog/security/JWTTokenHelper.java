package com.nithish.blog.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTTokenHelper {
    public static final long JWR_TOKEN_VALIDITY = 5 * 60 * 60;

    private String secret = "nkM59FPVTf1lEmaSfWooAHLHQUq3l2o+MBrfUQDeA800eGgy1tYlt17bD2QpcFGYQGVgfZFQPBcNSS0BFuKjUw==";

    // retrieve username from jwt token
    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    // retrieve expiration date from token
    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token,Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(getSigninkey()).build().parseSignedClaims(token).getPayload();
    }

    //check if the token has expired
    private  Boolean isTokenExpire(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token from user
    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();
        return doGenerateToken(claims,userDetails.getUsername());
    }

    //while creating the token
    //1.define claims of the token, like issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HSS12 algorithm and secret key.
    //3. According to JWS Compact Serialization
    // compaction of the JWT to a URL-safe string
    private  String doGenerateToken(Map<String,Object> claims, String subject){
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + JWR_TOKEN_VALIDITY * 100))
                .signWith(getSigninkey(),SignatureAlgorithm.HS512).compact();
    }

    private Key getSigninkey() {
        byte[] keysByte = Decoders.BASE64.decode(this.secret);
        return Keys.hmacShaKeyFor(keysByte);
    }

    public Boolean validateToken(String token,UserDetails userDetails){
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpire(token));
    }
}

