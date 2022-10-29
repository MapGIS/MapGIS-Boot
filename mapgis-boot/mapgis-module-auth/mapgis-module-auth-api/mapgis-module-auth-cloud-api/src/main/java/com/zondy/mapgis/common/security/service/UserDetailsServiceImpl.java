package com.zondy.mapgis.common.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户验证处理
 * 跟common-local-security中同名，为了使CasSecurityConfig在微服务中编译通过，后面微服务全面切换到spring security后，可以被common-local-security中替换
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
