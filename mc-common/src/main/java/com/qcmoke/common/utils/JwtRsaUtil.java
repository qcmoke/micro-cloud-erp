package com.qcmoke.common.utils;

import com.qcmoke.common.utils.security.RSAUtil;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;

/**
 * @author qcmoke
 */
public class JwtRsaUtil {
    private static final Log logger = LogFactory.getLog(JwtRsaUtil.class);

    /**
     * 生成jwt token（使用私钥加密）
     *
     * @param expData       载荷中的数据
     * @param privateKey    私钥
     * @param expireMinutes 过期时间，单位秒
     * @return jwt token
     */
    public static String generateToken(Map<String, Object> expData, PrivateKey privateKey, int expireMinutes) {
        return Jwts.builder()
                .setClaims(expData)
                .setExpiration(DateUtils.addMinutes(new Date(), expireMinutes))
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    /**
     * 公钥解析并校验jwt token
     *
     * @param token     用户请求中的token
     * @param publicKey 公钥
     * @return Jws<Claims>
     * @throws ExpiredJwtException      ExpiredJwtException
     * @throws UnsupportedJwtException  UnsupportedJwtException
     * @throws MalformedJwtException    MalformedJwtException
     * @throws SignatureException       SignatureException
     * @throws IllegalArgumentException IllegalArgumentException
     */
    public static Jws<Claims> parserAndVerifyToken(String token, PublicKey publicKey) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }

    /**
     * 获取令牌过期时间（即使过期异常一样强制获取）
     *
     * @param token 用户请求中的token
     * @param key   公钥
     * @return 过期时间
     */
    public static Date getExpired(String token, PublicKey key) {
        try {
            return parserAndVerifyToken(token, key).getBody().getExpiration();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getExpiration();
        } catch (Exception e) {
            logger.error("e={}", e);
        }
        return null;
    }


    /**
     * 令牌是否过期（即使过期异常一样强制获取）
     *
     * @param token     用户请求中的token
     * @param publicKey 公钥
     * @return boolean
     */
    public static boolean isExpired(String token, PublicKey publicKey) {
        try {
            return parserAndVerifyToken(token, publicKey).getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return e.getClaims().getExpiration().before(new Date());
        } catch (Exception e) {
            logger.error("e={}", e);
        }
        return false;
    }


    /**
     * 获取用户信息（即使过期异常一样强制获取）
     *
     * @param token     用户请求中的token
     * @param publicKey 公钥
     * @return 用户信息
     */
    public static Map<String, Object> getInfoFromToken(String token, PublicKey publicKey) {
        try {
            return parserAndVerifyToken(token, publicKey).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (Exception e) {
            logger.error("e={}", e);
        }
        return null;
    }


    public static void main(String[] args) {
        String jwtToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOjE3LCJ1c2VyX25hbWUiOiJxY21va2UiLCJzY29wZSI6WyJhbGwiXSwiZXhwIjoxNTgxMzE3ODM1LCJhdXRob3JpdGllcyI6WyJST0xFX21hbmFnZXIiLCJST0xFX3VzZXIiXSwianRpIjoiY2E4OGY5OGUtNzQ2MC00YmU0LWE4ZGItZGVhMzBkMzBjYjY1IiwiY2xpZW50X2lkIjoic3dhZ2dlciJ9.Xs0ejsz621HjCTVjdtIPXfJfAF5l6cM2d-7RDRbeAM0ppLNZgVAkw3gAvVcgjPiJ7vNgwizaA_XgdIiNAc2Sk-JN5mhwMtW0RZ-fTnh7QwV0mqPYuQ_jDeBo_Gy1_ej5iOkq5XsoDXuyn8c4utzZO-7dsUUKp_kBD9WjR_qD-Gr8nuEg4lvaXiLXT8CIhHnz8ec4XQ387nfpdOdfSD0FVUrI3Bx1aCRA5Rra8n51wQAqsduAjbeO_KouSWcKQo_Ch4Di0wZ2QZN4uiTpcyx3LnXS7fTISKBtzgwrwMQxCSpxEvPRGqenEbXBg-pEoJzKt-6gLLfw2pcle5MBT8oXgA";


        String pemKey = "-----BEGIN PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwlJSUTtDeUML9LYp6X/r\n" +
                "5/3LxdKaLLwpTPr+OHPYjRnYCdYXpG/CN13A+fubmX0Hs8zLfSN7Y+Ao93g1VDou\n" +
                "Y6FPcrDqoQ71AuH7lZG2eqHyOaOYKgfUSAG8N6I2OWZ7z1t1dRSBJbioP0xAic+O\n" +
                "8O31Mej23l64yuV9GogWRu0XjKam5fLumWLMmGGlMVYYZHUgX1AFbZQzgrophmug\n" +
                "dm5EOQWtok6Y3tS2/vbjVh/x/MNFMWsUVFjMi9t8UUU1v2S7twL/oPDXIc+lqoPX\n" +
                "UqqGXX5EZ0zn+yzwx2Ge53vTTut64wgrZCYuothNU0ULkfSVoW8SjJUjSwPJAKDI\n" +
                "0wIDAQAB\n" +
                "-----END PUBLIC KEY-----";
        PublicKey publicKey = RSAUtil.getPublicKeyFromPemEncoded(pemKey);
        System.out.println(getExpired(jwtToken, publicKey));
        System.out.println(isExpired(jwtToken, publicKey));
        System.out.println(getInfoFromToken(jwtToken, publicKey));

    }
}