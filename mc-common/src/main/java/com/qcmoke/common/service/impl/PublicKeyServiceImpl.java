package com.qcmoke.common.service.impl;

import com.qcmoke.common.service.PublicKeyService;
import com.qcmoke.common.utils.OauthSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;

/**
 * 获取公钥服务（需要认证服务器提前启动）
 *
 * @author qcmoke
 */
@Slf4j
public class PublicKeyServiceImpl implements PublicKeyService {

    private String publicKey;

    /**
     * 启动应用向认证服务器获取公钥并保存到内存（publicKey）中
     */
    @PostConstruct
    public synchronized void init() {
        publicKey = OauthSecurityUtil.getPublicKeyFromAuthServer();
        if (publicKey == null) {
            throw new RuntimeException("无法从认证服务器获取公钥");
        }
        log.info("get publicKey from auth server!,publicKey={}", publicKey);
    }

    /**
     * 获取公钥（需要认证服务器提前启动）
     *
     * @return 公钥
     */
    @Override
    public String getPublicKey() {
        if (StringUtils.isNoneBlank(publicKey)) {
            return publicKey;
        }
        init();
        return publicKey;
    }
}
