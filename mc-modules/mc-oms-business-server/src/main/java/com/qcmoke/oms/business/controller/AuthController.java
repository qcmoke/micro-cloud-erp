package com.qcmoke.oms.business.controller;

import com.qcmoke.oms.business.client.AuthClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * @author qcmoke
 */
@Slf4j
@Configuration
@RestController
public class AuthController {


    //    private static final String REST_URL_PREFIX = "http://39.106.195.202:9090";
    private static final String REST_URL_PREFIX = "http://MC-AUTH-SERVER";


    @Autowired
    private AuthClient authClient;

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/user")
    Object currentUser(HttpServletRequest request) {
        Object currentUser = authClient.currentUser();
        log.info("currentUser={}", currentUser);
        return currentUser;
    }


    /**

     HttpHeaders headers = new HttpHeaders();
     headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
     headers.set("Authorization", request.getHeader("Authorization"));
     HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(null, headers);
     Map<String, String[]> map = request.getParameterMap();
     ResponseEntity<Object> response = restTemplate.exchange(
     REST_URL_PREFIX + "/resource/user",
     HttpMethod.GET,
     entity,
     Object.class);

     return response.getBody();



     */
}
