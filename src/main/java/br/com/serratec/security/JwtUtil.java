package br.com.serratec.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final Key chave;
    private final long expiracaoMs;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration-ms:3600000}") long expiracaoMs) {
        this.chave = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiracaoMs = expiracaoMs;
    }

    public String gerarToken(String username, List<String> roles) {
        Date agora = new Date();
        Date expira = new Date(agora.getTime() + expiracaoMs);
        String rolesClaim = String.join(",", roles == null ? List.of() : roles);
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", rolesClaim)
                .setIssuedAt(agora)
                .setExpiration(expira)
                .signWith(chave, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validarToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(chave).build().parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public String obterUsuarioDoToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(chave).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public List<String> obterRolesDoToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(chave).build().parseClaimsJws(token).getBody();
        String roles = claims.get("roles", String.class);
        if (roles == null || roles.isBlank()) return List.of();
        return Arrays.stream(roles.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}