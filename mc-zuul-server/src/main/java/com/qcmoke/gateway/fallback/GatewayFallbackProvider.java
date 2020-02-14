//package com.qcmoke.gateway.fallback;
//
//import com.alibaba.fastjson.JSONObject;
//import com.qcmoke.common.dto.Result;
//import com.qcmoke.gateway.util.ZuulUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.client.ClientHttpResponse;
//import org.springframework.stereotype.Component;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
///**
// * 服务降级处理
// * 这个类用于做服务降级处理,可以自定义结果，为了方便这里直接返回错误数据给客户端了，这里不返回错误信息，GatewayExceptionFilter也会做处理，在这里做处理，只是为了把降级处理和错误处理进行区分而已。
// *
// * @author qcmoke
// */
//@Slf4j
//@Component
//public class GatewayFallbackProvider implements FallbackProvider {
//
//    /**
//     * 对所有服务可用
//     *
//     * @return
//     */
//    @Override
//    public String getRoute() {
//        return "*";
//    }
//
//
//    @Override
//    public ClientHttpResponse fallbackResponse(String route, Throwable e) {
//        Result<Object> error = ZuulUtil.buildErrorResult(e);
//        log.error("e={}", error.getMessage());
//        return getClientHttpResponse(error);
//    }
//
//
//    /**
//     * 响应客户端降级信息
//     */
//    private <T> ClientHttpResponse getClientHttpResponse(Result<T> error) {
//        return new ClientHttpResponse() {
//            @Override
//            public HttpStatus getStatusCode() throws IOException {
//                return HttpStatus.valueOf(error.getStatus());
//            }
//
//            @Override
//            public int getRawStatusCode() throws IOException {
//                return error.getStatus();
//            }
//
//            @Override
//            public String getStatusText() throws IOException {
//                return error.getMessage();
//            }
//
//            @Override
//            public void close() {
//
//            }
//
//            @Override
//            public InputStream getBody() throws IOException {
//                return new ByteArrayInputStream(JSONObject.toJSONString(error).getBytes());
//            }
//
//            @Override
//            public HttpHeaders getHeaders() {
//                HttpHeaders headers = new HttpHeaders();
//                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//                return headers;
//            }
//        };
//    }
//}
