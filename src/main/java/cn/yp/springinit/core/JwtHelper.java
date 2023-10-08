package cn.yp.springinit.core;

import cn.yp.springinit.cache.RedisClient;
import cn.yp.springinit.common.ResCode;
import cn.yp.springinit.exception.CustomException;
import cn.yp.springinit.utils.JsonUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author yp
 * @date: 2023/10/4
 */
@Component
@Slf4j
public class JwtHelper {
    @Component
    @Data
    @ConfigurationProperties("springinit.jwt")
    public static class JwtProperties {
        /**
         * 签发人
         */
        private String issuer;
        /**
         * 密钥
         */
        private String secret;
        /**
         * 有效期，毫秒时间戳
         */
        private Long expire = 2592000000L;
    }

    private final JwtProperties jwtProperties;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public JwtHelper(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.algorithm = Algorithm.HMAC256(jwtProperties.getSecret());
        this.verifier = JWT.require(algorithm).withIssuer(jwtProperties.getIssuer()).build();
    }

    public String genToken(Long userId) {
        // 1. 使用 java-jwt 生成签名
        Map<String, Long> payload = new HashMap<>();
        payload.put("userId", userId);
        String token = JWT.create().withIssuer(jwtProperties.getIssuer())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getExpire()))
                .withPayload(payload)
                .sign(algorithm);

        // 2. 将前面存储到 Redis，用于将无状态的token主动失效，注意时间单位
        RedisClient.setStrWithExpire(token, String.valueOf(userId), jwtProperties.getExpire() / 1000);

        return token;
    }

    public void removeToken(String token) {
        RedisClient.del(token);
    }

    public Long getUserIdByToken(String token) {
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            String payload = new String(Base64Utils.decodeFromString(decodedJWT.getPayload()));
            String userId = String.valueOf(JsonUtil.toObj(payload, HashMap.class).get("userId"));
            String user = RedisClient.getStr(token);
            if (user == null || !Objects.equals(user, userId)) {
                throw new CustomException(ResCode.JWT_ERROR, "JWT 错误或过期");
            }
            return Long.valueOf(userId);
        } catch (Exception e) {
            log.info("jwt 校验失败！ token: {}, msg: {}", token, e.getMessage());
            throw new CustomException(ResCode.JWT_ERROR, "校验失败");
        }
    }

}
