package cn.bitoffer.common.utils.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private static final String SECRET = "JOE38R39GNGRTU49Y534YNIGEYR534YNDEUR7964GEUR735";

    public static Map<String,Object> validateToken(final String token) {
        byte[] secretBytes = SECRET.getBytes(StandardCharsets.UTF_8);
        Key signingKey = new SecretKeySpec(secretBytes,SignatureAlgorithm.HS256.getJcaName());
        return Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token).getBody();
    }

    public static String generateToken(String username,String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId",userId);
        return createToken(claims, username);
    }

    private static String createToken(Map<String, Object> claims, String username) {
        byte[] secretBytes = SECRET.getBytes(StandardCharsets.UTF_8);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(Instant.now().toEpochMilli()))
                .setExpiration(new Date(Instant.now().toEpochMilli() + 1000 * 60 * 120)) //两小时有效
                .signWith(SignatureAlgorithm.HS256,secretBytes)
                .compact();
    }
}
