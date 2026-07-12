package com.itbin.utils;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.Map;

/**
 * JWT令牌工具类，提供生成和解析JWT的方法
 */
public final class JwtUtils {

    // JWT签名密钥（与测试代码一致）
    private static final String SECRET_KEY = "aXRiaW4=";

    // 令牌过期时间：12小时
    private static final long EXPIRATION_TIME = 12 * 60 * 60 * 1000L;

    // 私有构造方法，防止实例化工具类
    private JwtUtils() {}

    /**
     * 生成JWT令牌
     * @param claims 要存储在令牌中的自定义数据（载荷）
     * @return 生成的JWT令牌字符串
     */
    public static String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                // 设置签名算法和密钥
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                // 添加自定义载荷数据
                .addClaims(claims)
                // 设置过期时间（当前时间 + 1分钟）
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                // 压缩生成令牌
                .compact();
    }

    /**
     * 解析JWT令牌，获取载荷数据
     * @param token JWT令牌字符串
     * @return 令牌中的载荷数据（Claims对象）
     * @throws ExpiredJwtException 令牌过期异常
     * @throws MalformedJwtException 令牌格式错误异常
     * @throws SignatureException 签名验证失败异常
     * @throws UnsupportedJwtException 不支持的JWT类型异常
     * @throws IllegalArgumentException 令牌为空或无效异常
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                // 指定签名密钥（必须与生成时一致）
                .setSigningKey(SECRET_KEY)
                // 解析令牌并获取载荷体
                .parseClaimsJws(token)
                .getBody();
    }
}