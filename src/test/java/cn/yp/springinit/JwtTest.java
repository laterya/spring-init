package cn.yp.springinit;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.junit.jupiter.api.Test;
import org.springframework.util.Base64Utils;

/**
 * @ClassName: JWTTest
 * @Description:
 * @date: 2023/10/4
 * @author: yp
 */
public class JwtTest {

    @Test
    void testCreate() {
        Algorithm algorithm = Algorithm.HMAC256("JwtTest");
        String token = JWT.create()
                .withIssuer("yp")
                .sign(algorithm);
        System.out.println(token);
    }

    @Test
    void veifyTest() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ5cCJ9.SbO8_BfQQsQJGKrWqwxRG6LwpgDwrptWqRjNdvTzxm4";
        DecodedJWT decodedJWT;
        Algorithm algorithm = Algorithm.HMAC256("JwtTest");
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("yp")
                .build();

        decodedJWT = verifier.verify(token);
        String pay = new String(Base64Utils.decodeFromString(decodedJWT.getPayload()));
        System.out.println(pay);
    }
}
