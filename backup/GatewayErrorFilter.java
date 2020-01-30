//package com.qcmoke.gateway.filter;
//
//import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.RateLimitExceededException;
//import com.netflix.zuul.context.RequestContext;
//import com.netflix.zuul.exception.ZuulException;
//import com.qcmoke.core.utils.RespBean;
//import com.qcmoke.core.utils.ResponseWriterUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
//import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//public class GatewayErrorFilter extends SendErrorFilter {
//
//    @Override
//    public Object run() {
//        try {
//            RequestContext ctx = RequestContext.getCurrentContext();
//            RespBean respBean = getRespBean(ctx.getThrowable());
//            log.error("Zull sendError：{}", respBean.getMsg());
//            ResponseWriterUtil.writeJson(respBean);
//        } catch (Exception ex) {
//            log.error("Zuul sendError", ex);
//        }
//        return null;
//    }
//
//    protected RespBean getRespBean(Throwable throwable) {
//        if (throwable instanceof RateLimitExceededException) {
//            return RespBean.error("请求太频繁,e=" + throwable.getMessage());
//        } else if (throwable.getCause() instanceof ZuulRuntimeException) {
//            return RespBean.error("请求服务超时,e=" + throwable.getMessage());
//        } else if (throwable.getCause() instanceof ZuulException) {
//            return RespBean.error("服务不可用,e=" + throwable.getMessage());
//        } else {
//            return RespBean.error("Zuul请求服务异常,e=" + throwable.getMessage());
//        }
//    }
//}
