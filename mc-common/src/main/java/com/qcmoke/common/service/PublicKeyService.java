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
     * @throws Exception Exception
     */
    String getPublicPemKey() throws Exception;

    /**
     * 获取公钥（需要认证服务器提前启动）
     *
     * @return 公钥
     * @throws Exception Exception
     */
    PublicKey getPublicKey() throws Exception;
}
