package com.qcmoke.oms.business.client;

import com.qcmoke.common.constant.GlobalServiceListConstant;
import com.qcmoke.oms.api.OrderApi;
import com.qcmoke.oms.business.client.fallback.OrderClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 *  1、理解熔断和降级：熔断和降级其实是一个过程的两个阶段。熔断是异常或者请求超时了自己取消处理，也就是熔断掉（可以理解为程序直接发生了异常而断开），当熔断完成就会出触发降级处理，我就是原有程序的替代方法，返回一个降了级别的响应，从而防止发生雪崩。
 *  2、使用Hystrix实现熔断和降级
 *  方式1：
 *       (1)添加Hystrix依赖：
 *         <dependency>
 *             <groupId>org.springframework.cloud</groupId>
 *             <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
 *         </dependency>
 *      （2）启动类添加注解：@EnableCircuitBreaker
 *      （3）在需要实现熔断降级的方法上添加注解@HystrixCommand,并实现降级方法：如下：
 *           //添加熔断注解@HystrixCommand(fallbackMethod = "getOrderFallback"),而降级方法的实现是fallbackMethod：
 *             * @GetMapping(value = "/get/{username}")
 *            @HystrixCommand(fallbackMethod = "getOrderFallback")
 *            public Result<List<OrderDto>> getOrder(@PathVariable("username") String username) {
 *               ....
 *               return Result.ok(Collections.singletonList(orderDto));
 *            }
 *           //服务熔断后执行降级处理
 *           public Result<List<OrderDto>> getOrderFallback(String username) {
 *               String msg = "查询失败，触发服务熔断处理!";
 *               log.error(msg);
 *               return Result.error(msg);
 *           }
 *
 *方式2：
 *       由于Feign内置了Hystrix，所以可以直接在Feign注解中直接使用Hystrix
 *       （1）添加Feign依赖
 *            <dependency>
 *                <groupId>org.springframework.cloud</groupId>
 *                <artifactId>spring-cloud-starter-openfeign</artifactId>
 *            </dependency>
 *       （2）添加注解@EnableFeignClients启用Feign
 *       （3）在接口中使用Feign注解，并指定熔断属性fallback或者fallbackFactory指定降级方法。
 *        如：@FeignClient(value = GlobalServiceListConstant.MC_OMS_SERVER_ID, fallbackFactory = OrderClientFallbackFactory.class,fallback = OrderClientImpl.class)
 *            public interface OrderClient extends OrderApi {
 *            }
 *            fallbackFactory需要实现FallbackFactory接并通过create()方法返回OrderClient的实现类。
 *            fallback的值则是OrderClient的实现类。通过返回值来确定降级方法的返回值。
 *            fallbackFactory和fallback的区别是fallbackFactory可以获得异常信息，而fallback不能。
 *
 * @author qcmoke
 *
 */
@FeignClient(value = GlobalServiceListConstant.MC_OMS_SERVER_ID, fallbackFactory = OrderClientFallbackFactory.class)
public interface OrderClient extends OrderApi {
}
