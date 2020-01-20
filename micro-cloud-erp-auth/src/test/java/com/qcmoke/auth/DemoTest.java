package com.qcmoke.auth;

import com.qcmoke.auth.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DemoTest {


    public static void main(String[] args) {
//        test1();
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJxY21va2UiLCJhdXRob3JpdGllcyI6ImFkbWluLCIsImlhdCI6MTU3OTUxNDc5OCwiZXhwIjoxNTc5NTE0OTE4fQ.mt2SAg3i8ssXe2gWoz79roAP7GNkhyxGyill5jLwa64";

        Date date = JwtUtil.getExpirationDateFromToken(token);
        System.out.println(date == null ? "null" : date.toLocaleString());
        System.out.println(JwtUtil.isTokenExpired(token));

        System.out.println(JwtUtil.refreshToken(token));

        date = JwtUtil.getExpirationDateFromToken(token);
        System.out.println(date == null ? "null" : date.toLocaleString());
        System.out.println(JwtUtil.isTokenExpired(token));
    }


    private static void test1() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("admin"));
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken("qcmoke", null, authorities);
        String token = JwtUtil.geneJsonWebToken(usernamePasswordAuthenticationToken);
        System.out.println(token);

        Claims claims = JwtUtil.getClaims(token);
        System.out.println(claims);
    }
}
