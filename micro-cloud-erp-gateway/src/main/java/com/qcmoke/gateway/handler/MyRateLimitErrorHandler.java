package com.qcmoke.gateway.handler;

import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.repository.DefaultRateLimiterErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 自定义限流错误请求返回格式
 */
@Slf4j
@Component
public class MyRateLimitErrorHandler extends DefaultRateLimiterErrorHandler {

    @Override
    public void handleError(String msg, Exception e) {
        log.info("too many request！！！ e={}", e.getMessage());
        //ResponseWriterUtil.writeJson(RespBean.error(e.getMessage()));
        //super.handleError(msg, e);
    }
}
