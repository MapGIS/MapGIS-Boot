package com.zondy.mapgis.common.security.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Chelsea
 * @since 2023/2/17 10:59
 */
public class HttpsSecurityHeaderFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.addHeader("Content-Security-Policy", "default-src 'self' https:; font-src 'self' https: data:; img-src 'self' https: data:; object-src 'none'; script-src https: 'unsafe-inline' 'unsafe-eval'; style-src 'self' https: 'unsafe-inline'");
        response.addHeader("X-Frame-Options", "SAMEORIGIN");
        response.addHeader("X-Content-Type-Options", "nosniff");
        response.addHeader("X-Download-Options", "noopen");
        response.addHeader("X-Permitted-Cross-Domain-Policies", "none");
        response.addHeader("X-Xss-Protection", "1 ; mode=block");
        filterChain.doFilter(servletRequest, response);
    }

    @Override
    public void destroy() {
    }
}
