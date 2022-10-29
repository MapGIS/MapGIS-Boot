package com.zondy.mapgis.auth.cas.config;

import com.zondy.mapgis.auth.cas.handler.CasAuthenticationSuccessHandlerImpl;
import com.zondy.mapgis.auth.cas.service.CasUserDetailsServiceImpl;
import com.zondy.mapgis.common.core.config.ProductConfig;
import com.zondy.mapgis.common.core.config.properties.ApiPathProperties;
import com.zondy.mapgis.common.security.filter.JwtAuthenticationTokenFilter;
import com.zondy.mapgis.common.security.handler.AuthenticationEntryPointImpl;
import com.zondy.mapgis.common.security.handler.LogoutSuccessHandlerImpl;
import com.zondy.mapgis.system.api.service.SysServiceProxy;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.filter.CorsFilter;

import java.util.Map;

/**
 * spring security cas默认配置
 *
 * @author powanjuanshu
 * @since 2022/6/9 16:18
 */
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class CasSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 资源映射路径前缀
     */
    @Value("${file.prefix:/file}")
    public String filePrefix;

    /**
     * 自定义用户认证逻辑
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 认证失败处理类
     */
    @Autowired
    private AuthenticationEntryPointImpl unauthorizedHandler;

    /**
     * 退出处理类
     */
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    /**
     * token认证过滤器
     */
    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

    /**
     * 跨域过滤器
     */
    @Autowired
    private CorsFilter corsFilter;

    /**
     * 产品配置
     */
    @Autowired
    private ProductConfig productConfig;

    /**
     * ApiPath属性
     */
    @Autowired
    private ApiPathProperties apiPathProperties;

    /**
     * 自定义用户认证逻辑
     */
    @Autowired
    private CasUserDetailsServiceImpl casUserDetailsService;

    /**
     * CAS认证成功处理类
     */
    @Autowired
    private CasAuthenticationSuccessHandlerImpl casAuthenticationSuccessHandler;

    /**
     * 系统服务代理
     */
    @Autowired
    private SysServiceProxy sysServiceProxy;

    /**
     * 解决 无法直接注入 AuthenticationManager
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        String strProductName = productConfig.getName();
        String strServicesPrefix = apiPathProperties.getServicesPrefix();
        String strManagerPrefix = apiPathProperties.getManagerPrefix();

        httpSecurity
                // CSRF禁用，因为不使用session
                .csrf().disable()
                // 认证失败处理类
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 过滤请求
                .authorizeRequests()
                // 对于登录login 注册register 验证码captchaImage 允许匿名访问
                .antMatchers(strServicesPrefix + "/auth/login", strServicesPrefix + "/auth/register", strServicesPrefix + "/auth/captchaImage").anonymous()
                .antMatchers(strServicesPrefix + "/auth/thirdLogin/**").anonymous()
                .antMatchers(strServicesPrefix + "/auth/casLogin/**").anonymous()
                // 静态资源，可匿名访问
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        filePrefix + "/**",
                        "/" + strProductName + "/static/**",
                        "/" + strProductName + "/manager/**"
                ).permitAll()
                .antMatchers(
                        "/swagger-ui.html",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/**/api-docs/**",
                        "/druid/**",
                        strManagerPrefix + "/system/webConfig/**")
                .permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable();
        // 添加Logout filter
        httpSecurity.logout().logoutUrl(strServicesPrefix + "/auth/logout").logoutSuccessHandler(logoutSuccessHandler);
        // 添加CAS 认证filter
        httpSecurity.addFilter(casAuthenticationFilter());
        // 添加JWT filter
        httpSecurity.addFilterBefore(authenticationTokenFilter, CasAuthenticationFilter.class);
        httpSecurity.addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter.class);

        // 添加CORS filter
        httpSecurity.addFilterBefore(corsFilter, JwtAuthenticationTokenFilter.class);
        httpSecurity.addFilterBefore(corsFilter, LogoutFilter.class);
    }

    /**
     * 强散列哈希加密实现
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 身份认证接口
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
        auth.authenticationProvider(casAuthenticationProvider());
    }

    /**
     * CAS认证过滤器
     */
    @Bean
    public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
        casAuthenticationFilter.setAuthenticationManager(authenticationManager());
        casAuthenticationFilter.setAuthenticationSuccessHandler(casAuthenticationSuccessHandler);
        // casAuthenticationFilter.setFilterProcessesUrl在SysConfigEvent事件响应中动态设置
        return casAuthenticationFilter;
    }

    /**
     * CAS认证Provider
     */
    @Bean
    public CasAuthenticationProvider casAuthenticationProvider() {
        CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
        casAuthenticationProvider.setAuthenticationUserDetailsService(casUserDetailsService);
        casAuthenticationProvider.setKey("casAuthenticationProviderKey");
        casAuthenticationProvider.setTicketValidator(new Cas20ServiceTicketValidator(""));
        // casAuthenticationFilter.setServiceProperties在SysConfigEvent事件响应中动态设置
        // casAuthenticationFilter.setTicketValidator在SysConfigEvent事件响应中动态设置
        return casAuthenticationProvider;
    }

    /**
     * 单点注销过滤器
     * 用于接收cas服务端的注销请求
     */
    @Bean
    public SingleSignOutFilter singleSignOutFilter() {
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        singleSignOutFilter.setIgnoreInitConfiguration(true);
        return singleSignOutFilter;
    }

    @Bean
    public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListener() {
        ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> servletListenerRegistrationBean = new ServletListenerRegistrationBean<>();
        servletListenerRegistrationBean.setListener(new SingleSignOutHttpSessionListener());
        return servletListenerRegistrationBean;
    }

    public void updateCasSecurityConfig() {
        Map<String, Object> casConfig = sysServiceProxy.getCasConfig();
        ServiceProperties serviceProperties = new ServiceProperties();
        CasAuthenticationProvider casAuthenticationProvider = casAuthenticationProvider();
        CasAuthenticationFilter casAuthenticationFilter = null;

        try {
            casAuthenticationFilter = casAuthenticationFilter();
        } catch (Exception e) {
            return;
        }

        // 设置cas客户端登录完整的url
        serviceProperties.setService((String) casConfig.get("casServiceLoginUrl"));
        serviceProperties.setAuthenticateAllArtifacts(true);

        casAuthenticationFilter.setFilterProcessesUrl((String) casConfig.get("casServiceLoginPath"));
        casAuthenticationProvider.setServiceProperties(serviceProperties);
        casAuthenticationProvider.setTicketValidator(new Cas20ServiceTicketValidator((String) casConfig.get("casServerUrl")));
    }
}
