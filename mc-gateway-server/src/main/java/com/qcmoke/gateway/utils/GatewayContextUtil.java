package com.qcmoke.gateway.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.util.LinkedHashSet;

/**
 * @author qcmoke
 */

public class GatewayContextUtil {
    private static final Log logger = LogFactory.getLog(GatewayContextUtil.class);

    /**
     * 获取客户端原始请求路径
     * <p>
     * 真实原始请求路径是路径重写前的客户端原始请求路径；网关操作的请求路径则是经过路径重写后的路径。
     *
     * @param exchange ServerWebExchange
     * @return 客户端原始请求路径
     */
    public static String getOriginalPath(ServerWebExchange exchange) {
        String originalPath = null;
        LinkedHashSet<URI> requiredUris = exchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        for (URI uri : requiredUris) {
            //其中scheme有两种：http开头的和lb开头的，其中htp开头地址就是客户端原始请求地址，而lb开头的则是网关操作的请求地址
            String scheme = uri.getScheme();
            if (scheme.startsWith("http")) {
                originalPath = uri.getPath();
            }
        }
        //为了保守起见如果找不到去掉前缀前的真实原始请求地址还是要返回去掉前缀后的请求地址
        if (originalPath == null) {
            logger.info("无法获取真实原始请求路径地址，使用网关操作的请求地址");
            originalPath = exchange.getRequest().getPath().value();
        }
        return originalPath;
    }


    /**
     * 获取当前请求匹配到的网关路由
     *
     * @param exchange ServerWebExchange
     * @return 当前请求匹配到的网关路由
     */
    public static Route getRoute(ServerWebExchange exchange) {
        return (Route) exchange.getAttributes().get(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
    }

    /**
     * 获取当前请求匹配到的网关路由id
     *
     * @param exchange ServerWebExchange
     * @return 当前请求匹配到的网关路由id
     */
    public static String getRouteId(ServerWebExchange exchange) {
        return getRoute(exchange).getId();
    }
}
