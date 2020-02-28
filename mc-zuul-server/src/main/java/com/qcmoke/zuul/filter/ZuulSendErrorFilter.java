package com.qcmoke.zuul.filter;

import com.netflix.zuul.context.RequestContext;
import com.qcmoke.common.vo.Result;
import com.qcmoke.common.utils.ResponseWriterUtil;
import com.qcmoke.zuul.util.ZuulUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.stereotype.Component;

/**
 * 服务 eureka-client 的异常退回处理类
 *
 * @author qcmoke
 */
@Slf4j
@Component
public class ZuulSendErrorFilter extends SendErrorFilter {

    private static final Logger logger = LoggerFactory.getLogger(ZuulSendErrorFilter.class);

    @Override
    public String filterType() {
        return "error";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        try {
            RequestContext context = RequestContext.getCurrentContext();
            Result<Object> error = ZuulUtil.buildErrorResult(context.getThrowable());
            log.error("e={}", error.getMessage());
            ResponseWriterUtil.writeJson(error);
        } catch (Exception var5) {
            String errorMsg = "服务异常,e=" + var5.getMessage();
            logger.error("e={}", errorMsg);
            ResponseWriterUtil.writeJson(Result.error(errorMsg));
        }
        return null;
    }
}
