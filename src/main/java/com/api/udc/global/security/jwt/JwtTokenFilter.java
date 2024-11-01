package com.api.udc.global.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String token = jwtTokenProvider.resolveToken(request);
        System.out.println("token: " +token);
        if (token != null) {
            if (jwtTokenProvider.validateToken(token)) {
                jwtTokenProvider.setSecurityContext(token);
            } else if (jwtTokenProvider.isTokenExpired(token)) {
                // 토큰이 만료된 경우 새 토큰 발급

                String username = jwtTokenProvider.getClaimsFromToken(token).getSubject();
                List<String> roles = jwtTokenProvider.getClaimsFromToken(token).get("auth", List.class);
                String newToken = jwtTokenProvider.refreshToken(username, roles);

                // 새 토큰을 응답 헤더에 추가
                response.setHeader("Authorization", "Bearer " + newToken);
                System.out.println("token expired" +token);
                // 새 토큰을 사용하여 보안 컨텍스트 설정
                jwtTokenProvider.setSecurityContext(newToken);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}