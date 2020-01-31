package com.qcmoke.stock.controller;

import com.qcmoke.common.utils.RespBean;
import com.qcmoke.stock.entity.CodeTokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequestMapping("/admin")
@Controller
public class AdminController {

    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/callback")
    public String callback(String code, String state, HttpSession session) {
        try {
            String oauthServiceUrl = "http://127.0.0.1:9090/oauth/token";
            LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>() {{
                add("code", code);
                add("grant_type", "authorization_code");
                add("redirect_uri", "http://127.0.0.1:8083/admin/callback");
            }};
            HttpHeaders headers = new HttpHeaders() {{
                setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                set("Authorization", getAuthorizationHeader("admin", "123456"));
            }};
            HttpEntity<LinkedMultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
            ResponseEntity<CodeTokenInfo> tokenInfoResponse = restTemplate.exchange(
                    oauthServiceUrl,
                    HttpMethod.POST,
                    entity,
                    CodeTokenInfo.class //将返回结果转化成CodeTokenInfo
            );

            CodeTokenInfo codeTokenInfo = tokenInfoResponse.getBody();
            log.info("codeTokenInfo={}", codeTokenInfo);
            session.setAttribute("codeTokenInfo", codeTokenInfo);
            return "redirect:/index.html";
        } catch (Exception e) {
            log.error("e={}", e.getMessage());
        }
        return "redirect:/error.html";
    }

    @ResponseBody
    @GetMapping("/me")
    public RespBean me(HttpSession session) {
        Object codeTokenInfo = session.getAttribute("codeTokenInfo");
        if (codeTokenInfo == null) {
            return RespBean.unauthorized("未认证");
        }
        return RespBean.ok(codeTokenInfo);
    }

    private String getAuthorizationHeader(String clientId, String clientSecret) {
        if (clientId == null || clientSecret == null) {
            log.warn("Null Client ID or Client Secret detected. Endpoint that requires authentication will reject request with 401 error.");
        }
        String creds = String.format("%s:%s", clientId, clientSecret);
        return "Basic " + new String(Base64.encode(creds.getBytes(StandardCharsets.UTF_8)));
    }
}
