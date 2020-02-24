package com.qcmoke.zuul.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 网关Cors跨域配置
 *
 * @author qcmoke
 */
@Configuration
public class ZuulCorsConfig {
    @Bean
    public FilterRegistrationBean<CorsFilter> filterRegistrationBean() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        // 允许cookies跨域
        config.setAllowCredentials(true);
        // 允许向该服务器提交请求的URI，*表示全部允许。。这里尽量限制来源域，比如http://xxxx:8080 ,以降低安全风险。。
        config.addAllowedOrigin("*");
        // 允许访问的头信息,*表示全部
        config.addAllowedHeader("*");
        // 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
        config.setMaxAge(18000L);
        // 允许提交请求的方法，*表示全部允许，也可以单独设置GET、PUT等
        config.addAllowedMethod("*");
        // 允许Get的请求方法 /*config.addAllowedMethod("GET");config.addAllowedMethod("PUT");config.addAllowedMethod("POST");config.addAllowedMethod("DELETE");config.addAllowedMethod("PATCH");config.addAllowedMethod("HEAD");*/
        source.registerCorsConfiguration("/**", config);
        CorsFilter corsFilter = new CorsFilter(source);
        FilterRegistrationBean<CorsFilter> filterRegistrationBean = new FilterRegistrationBean<>(corsFilter);
        // 解决 zuul+oauth2 跨域配置 C - Cross  O - Origin  R - Resource  S - Sharing,无法获取token的问题，https://www.cnblogs.com/1994july/p/12038723.html
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterRegistrationBean;
    }
}
