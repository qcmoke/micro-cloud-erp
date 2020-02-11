package com.qcmoke.common.service;

import java.security.PublicKey;

/**
 * @author qcmoke
 */
public interface PublicKeyService {

    /**
     * 获取公钥（需要认证服务器提前启动）
     *
     * @return 公钥
     */
    String getPublicPemKey();

    /**
     * 获取公钥（需要认证服务器提前启动）
     *
     * @return 公钥
     */
    PublicKey getPublicKey();
}
