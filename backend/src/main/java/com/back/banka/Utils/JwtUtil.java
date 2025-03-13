package com.back.banka.Utils;

import com.back.banka.Model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
public class JwtUtil {


    private final Key key;
    private final long expirationTime;
    private final Long jwtRefreshExpirationTime;
    private final long resetPasswordTokenExpirationTime;


    public JwtUtil(@Value("${jwt.secret}")
                   String secret,
                   @Value("${jwt.expiration}")
                   long expiration,
                   @Value("${jwt.refresh-expiration}")
                   Long jwtRefreshExpirationTime,
                   @Value("${jwt.reset-password-expiration}")
                   long resetPasswordTokenExpirationTime

    ){
        this.resetPasswordTokenExpirationTime = resetPasswordTokenExpirationTime;

        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes); //Generando clave con HS256
        this.expirationTime = expiration;
        this.jwtRefreshExpirationTime = jwtRefreshExpirationTime;
    }

    public void diagnosticCheck(String token) {
        try {
            log.info("Clave actual (Base64): {}",
                    java.util.Base64.getEncoder().encodeToString(this.key.getEncoded()));

            Claims claims = getClaims(token);
            log.info("Token verificado exitosamente");
            log.info("Email en token: {}", claims.getSubject());
            log.info("Fecha de emisión: {}", claims.getIssuedAt());
            log.info("Fecha de expiración: {}", claims.getExpiration());

        } catch (ExpiredJwtException e) {
            log.error("Token expirado: {}", e.getMessage());
        } catch (SignatureException e) {
            log.error("Error en la firma del token: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Token mal formado: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado en verificación: {}", e.getMessage(), e);
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

    public String generateResetPasswordToken(String email){
        return generateToken(email, resetPasswordTokenExpirationTime);
    }

    public boolean isTokenValid(String token, UserDetails user) {
        final String username = extractEmail(token);
        if (username == null) {
            log.warn("No se pudo extraer el email del token.");
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
        } catch (JwtException e){
            log.error("Token invalido {}", e.getMessage());
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

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getClaims(token);
        return claimsResolver.apply(claims);
    }

}
