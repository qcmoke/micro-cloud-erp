package com.qcmoke.common.service.impl;

import com.qcmoke.common.service.PublicKeyService;
import com.qcmoke.common.utils.oauth.OauthSecurityUtil;
import com.qcmoke.common.utils.security.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 * 获取公钥服务（需要认证服务器提前启动）
 *
 * @author qcmoke
 */
@Slf4j
public class PublicKeyServiceImpl implements PublicKeyService {

    private String publicPemKey;
    private PublicKey publicKey;

    private String oauthServiceIp;
    private String clientId;
    private String clientSecret;
    private String oauthServicePort;


    public PublicKeyServiceImpl(String oauthServiceIp, String oauthServicePort, String clientId, String clientSecret) {
        this.oauthServiceIp = oauthServiceIp;
        this.oauthServicePort = oauthServicePort;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    /**
     * 启动应用向认证服务器获取公钥并保存到内存（publicKey）中
     */
    @PostConstruct
    public synchronized void init() throws Exception {
        publicPemKey = OauthSecurityUtil.getPublicKeyFromAuthServer(oauthServiceIp, oauthServicePort, clientId, clientSecret);
        if (publicPemKey == null) {
            throw new Exception("无法从认证服务器获取公钥");
        }
        publicKey = RSAUtils.getPublicKeyFromPemEncoded(publicPemKey);
        log.info("get publicKey from auth server!,publicPemKey={}", publicPemKey);
    }

    /**
     * 获取公钥（需要认证服务器提前启动）
     *
     * @return 公钥
     */
    @Override
    public String getPublicPemKey() throws Exception {
        if (StringUtils.isNoneBlank(publicPemKey)) {
            return publicPemKey;
        }
        init();
        return publicPemKey;
    }

    /**
     * 获取公钥（需要认证服务器提前启动）
     *
     * @return 公钥
     */
    @Override
    public PublicKey getPublicKey() throws Exception {
        if (publicPemKey != null) {
            return publicKey;
        }
        init();
        return publicKey;
    }
}
