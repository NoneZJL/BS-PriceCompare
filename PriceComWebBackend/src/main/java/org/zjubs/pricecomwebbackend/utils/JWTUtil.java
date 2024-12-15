package org.zjubs.pricecomwebbackend.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.zjubs.pricecomwebbackend.query.ApiResult;

import java.util.Date;
import java.util.Map;

public class JWTUtil {
    private static String SIGNAYURE = "ZJUBS";

    /**
     * @param map:
     * @return String
     * @description 生成token
     */
    public static String getToken(Map<String, String> map) {
        JWTCreator.Builder builder = JWT.create();
        map.forEach(builder::withClaim);
        builder.withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000));
        return builder.sign(Algorithm.HMAC256(SIGNAYURE));
    }

    /**
     * @param token:
     * @description 验证token
     */
    public static void verify(String token) {
        JWT.require(Algorithm.HMAC256(SIGNAYURE)).build().verify(token);
    }

    /**
     * @param token:
     * @return DecodedJWT
     * @description 解析token
     */
    public static DecodedJWT getToken(String token) {
        return JWT.require(Algorithm.HMAC256(SIGNAYURE)).build().verify(token);
    }

    public static String getTokenByIdAndUsername(Integer id, String username) {
        JWTCreator.Builder builder = JWT.create();
        builder.withClaim("id", id).withClaim("username", username);
        builder.withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000));
        return builder.sign(Algorithm.HMAC256(SIGNAYURE));
    }

    public static Integer getIdByToken(String token) {
        return JWT.require(Algorithm.HMAC256(SIGNAYURE)).build().verify(token).getClaim("id").asInt();
    }

    public static String getUsernameByToken(String token) {
        return JWT.require(Algorithm.HMAC256(SIGNAYURE)).build().verify(token).getClaim("username").asString();
    }

    public static String getTokenByEmailAndJustifyCode(String email, String justifyCode) {
        JWTCreator.Builder builder = JWT.create();
        builder.withClaim("email", email).withClaim("justifyCode", justifyCode);
        builder.withExpiresAt(new Date(System.currentTimeMillis() + 300 * 1000));
        return builder.sign(Algorithm.HMAC256(SIGNAYURE));
    }

    public static String getEmailByToken(String token) {
        return JWT.require(Algorithm.HMAC256(SIGNAYURE)).build().verify(token).getClaim("email").asString();
    }

    public static String getJustifyCodeByToken(String token) {
        return JWT.require(Algorithm.HMAC256(SIGNAYURE)).build().verify(token).getClaim("justifyCode").asString();
    }

    public static boolean checkNotLogin(ApiResult apiResult) {
        return apiResult.message.equals("JWTerror");
    }
}
