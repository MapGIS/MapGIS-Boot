package com.zondy.mapgis.common.security.filter;

import com.zondy.mapgis.common.security.service.TokenService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token过滤器 验证token有效性
 * 跟common-local-security中同名，为了使CasSecurityConfig在微服务中编译通过，后面微服务全面切换到spring security后，可以被common-local-security中替换
 *
 * @author xiongbo
 * @since 2022/6/10 13:47
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private TokenService tokenService;

    public JwtAuthenticationTokenFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        chain.doFilter(request, response);
    }
}
