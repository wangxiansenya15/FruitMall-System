package com.wxs.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类，用于生成和解析JWT令牌
 * @author Arthur Wang
 * @date 2025/5/14 20:55
 */
@Component
@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtUtils {

    // 从配置文件中注入的JWT密钥
    @Value("${jwt.secret}")
    private String secretString;
    
    // 密钥对象，延迟初始化
    private SecretKey secretKey;

    // 从配置文件中注入的JWT过期时间（秒）
    @Value("${jwt.expire}")
    private long expire;
    
    // 令牌前缀
    private static final String TOKEN_PREFIX = "Bearer ";
    
    // 请求头中的令牌键名
    private static final String HEADER_KEY = "Authorization";

    /**
     * 获取密钥对象
     * 使用安全的密钥生成方法，确保密钥长度符合JWT JWA规范(RFC 7518, Section 3.2)
     * 
     * @return 密钥对象
     */
    private SecretKey getSecretKey() {
        if (secretKey == null) {
            // 方法一：如果配置的密钥字符串足够长且安全，则直接使用
            if (secretString.length() >= 32) { // 至少256位(32字节)
                secretKey = Keys.hmacShaKeyFor(secretString.getBytes());
            } else {
                // 方法二：使用Keys.secretKeyFor方法生成安全的密钥
                // 这将自动生成一个符合HMAC-SHA-256算法要求的安全密钥
                secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            }
        }
        return secretKey;
    }
    
    /**
     * 生成JWT令牌
     *
     * @param username 用户名，作为JWT的主体
     * @return 生成的JWT令牌字符串
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)  // 设置JWT的主体为用户名
                .setIssuedAt(new Date())  // 设置JWT的发行时间为当前时间
                .setExpiration(new Date(System.currentTimeMillis() + expire * 1000))  // 设置JWT的过期时间
                .signWith(getSecretKey())  // 使用密钥对象签名JWT
                .compact();  // 压缩JWT并返回
    }
    
    /**
     * 生成带有自定义声明的JWT令牌
     *
     * @param username 用户名，作为JWT的主体
     * @param claims 自定义声明信息
     * @return 生成的JWT令牌字符串
     */
    public String generateToken(String username, Map<String, Object> claims) {
        if (claims == null) {
            claims = new HashMap<>();
        }
        return Jwts.builder()
                .setClaims(claims)  // 设置自定义声明
                .setSubject(username)  // 设置JWT的主体为用户名
                .setIssuedAt(new Date())  // 设置JWT的发行时间为当前时间
                .setExpiration(new Date(System.currentTimeMillis() + expire * 1000))  // 设置JWT的过期时间
                .signWith(getSecretKey())  // 使用密钥对象签名JWT
                .compact();  // 压缩JWT并返回
    }

    /**
     * 解析JWT令牌，提取其中的声明
     *
     * @param token JWT令牌字符串
     * @return JWT中的声明
     */
    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())  // 使用密钥对象验证JWT签名
                .build()
                .parseClaimsJws(token)  // 解析JWT并验证签名
                .getBody();  // 提取并返回JWT中的声明
    }

    /**
     * 检查JWT令牌是否已经过期
     *
     * @param token JWT令牌字符串
     * @return 如果令牌已过期则返回true，否则返回false
     */
    public boolean isTokenExpired(String token) {
        try {
            return parseToken(token).getExpiration().before(new Date());  // 比较JWT的过期时间和当前时间
        } catch (ExpiredJwtException e) {
            return true;
        }
    }
    
    /**
     * 从请求中获取JWT令牌
     *
     * @param request HTTP请求
     * @return JWT令牌字符串，如果没有找到则返回null
     */
    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER_KEY);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            // 去除Bearer前缀
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
    
    /**
     * 从令牌中获取用户名
     *
     * @param token JWT令牌字符串
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        return parseToken(token).getSubject();
    }
    
    /**
     * 验证令牌是否有效
     *
     * @param token JWT令牌字符串
     * @return 如果令牌有效则返回true，否则返回false
     */
    public boolean validateToken(String token) {
        try {
            // 解析令牌会验证签名和过期时间
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 刷新令牌
     *
     * @param token 原JWT令牌字符串
     * @return 新的JWT令牌字符串
     */
    public String refreshToken(String token) {
        Claims claims = parseToken(token);
        claims.setIssuedAt(new Date());
        claims.setExpiration(new Date(System.currentTimeMillis() + expire * 1000));
        return Jwts.builder()
                .setClaims(claims)
                .signWith(getSecretKey())
                .compact();
    }
}
