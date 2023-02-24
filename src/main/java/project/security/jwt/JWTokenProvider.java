package project.security.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import project.model.entity.user.UserEntity;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JWTokenProvider.class);

    @Value("${jwt.secret.key.access}")
    private String jwtAccessSecret;

    @Value("${jwt.secret.key.refresh}")
    private String jwtRefreshSecret;

    @Value("${jwt.secret.expiration.access}")
    private int accessTokenExpiration;

    @Value("${jwt.secret.expiration.refresh}")
    private int refreshTokenExpiration;

    public String extractUsername (String token) {
        return extractClaim(token,Claims::getSubject);
    }

    public <T> T extractClaim (String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims (String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid (String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired (String token) {
        return extractExpired(token).before(new Date());
    }

    private Date extractExpired (String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateAccessToken (UserDetails userDetails){
        return generateAccessToken(new HashMap<>(),userDetails);
    }

    public String generateAccessToken (
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ){
        return "Bearer " +Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getSignInKey(),SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey() {
        byte[] decode = Decoders.BASE64.decode(jwtAccessSecret);
        return Keys.hmacShaKeyFor(decode);
    }

}
