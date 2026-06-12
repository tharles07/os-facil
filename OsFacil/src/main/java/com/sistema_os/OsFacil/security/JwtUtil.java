package com.sistema_os.OsFacil.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sistema_os.OsFacil.model.Usuario;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final long EXPIRATION = 1000 * 60 * 60; // 1 hora

    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 64) {
            throw new IllegalArgumentException("jwt.secret deve ter pelo menos 64 bytes para HS512");
        }

        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /* ==========================
       GERAR TOKEN
    ========================== */
    public String gerarToken(Usuario user) {

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("perfil", user.getPerfil())
                .claim("empresaId", user.getEmpresa().getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }


    /* ==========================
       VALIDAR TOKEN
    ========================== */
    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /* ==========================
       EXTRAIR USERNAME
    ========================== */
    public String extrairUsername(String token) {
        return getClaims(token).getSubject();
    }
    /* ==========================
    EXTRAIR EMPRESA ID
    ========================== */
    public Long extrairEmpresaId(String token) {
        Object value = getClaims(token).get("empresaId");

        if (value == null) {
            throw new RuntimeException("empresaId não encontrado no token");
        }

        return ((Number) value).longValue();
    }

    /* ==========================
       EXTRAIR PERFIL
    ========================== */
    public String extrairPerfil(String token) {
        return getClaims(token).get("perfil", String.class);
    }

    /* ==========================
       CLAIMS
    ========================== */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
