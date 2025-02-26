package com.back.banka.Utils;

import com.back.banka.Model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;

@Service
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private final Key key;
    private final long expirationTime;
    private final Long jwtRefreshExpirationTime;

    public JwtUtil(@Value("${JWT_SECRET}")
                   String secret,
                   @Value("${JWT_EXPIRATION}")
                   long expiration,
                   @Value("${JWT_REFRESH_EXPIRATION}")
                   Long jwtRefreshExpirationTime
    ){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes); //Generando clave con HS256
        this.expirationTime = expiration;
        this.jwtRefreshExpirationTime = jwtRefreshExpirationTime;
    }

    public void diagnosticCheck(String token) {
        try {
            logger.info("Clave actual (Base64): {}",
                    java.util.Base64.getEncoder().encodeToString(this.key.getEncoded()));

            Claims claims = getClaims(token);
            logger.info("Token verificado exitosamente");
            logger.info("Email en token: {}", claims.getSubject());
            logger.info("Fecha de emisión: {}", claims.getIssuedAt());
            logger.info("Fecha de expiración: {}", claims.getExpiration());

        } catch (ExpiredJwtException e) {
            logger.error("Token expirado: {}", e.getMessage());
        } catch (SignatureException e) {
            logger.error("Error en la firma del token: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Token mal formado: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado en verificación: {}", e.getMessage(), e);
        }
    }

    public String generateToken(String email, Long expiration){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))//Fecha de creación del token
                .setExpiration(new Date(System.currentTimeMillis() + expiration))// Tiempo en que va a expirar el Token
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)//Se usa HS256
                .compact();
    }

    public String generateRefreshToken(String email){
        return generateToken(email, jwtRefreshExpirationTime);
    }

    public String generateAccessToken(String email){
        return generateToken(email, expirationTime);
    }

    public boolean isTokenValid(String token, UserDetails user) {
        final String username = extractEmail(token);
        if (username == null) {
            logger.warn("No se pudo extraer el email del token.");
            return false;
        }
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
    public String extractEmail(String token){
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token){
        try{
            getClaims(token);
            return true;
        } catch (ExpiredJwtException e){
            System.out.println("Token expirado:" + e.getMessage());
        } catch (JwtException e){
            System.out.println("Token Inválido" + e.getMessage());
        }
        return false;
    }

    //Para la validación del token

    public String ValidateTokenAndGetMessage(String token){
        return validateToken(token)?"Token válido":"Token inválido";
    }

    public Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private Key getSigninKey() {
        return this.key;
    }


}
