package com.zondy.mapgis.auth.cas.service;

import com.zondy.mapgis.auth.api.service.SysLoginService;
import com.zondy.mapgis.auth.cas.model.CasLoginUser;
import com.zondy.mapgis.common.core.exception.ServiceException;
import com.zondy.mapgis.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用于加载用户信息,实现AuthenticationUserDetailsService接口
 *
 * @author powanjuanshu
 * @since 2022/6/7 8:44
 */
@Service
public class CasUserDetailsServiceImpl implements AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {

    @Autowired
    private SysLoginService loginService;

    @Override
    public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException {
        String username = token.getName();
        try {
            LoginUser loginUser = loginService.loadUserByUsername(username);

            loginService.afterSuccessLogin(loginUser, username);

            CasLoginUser casLoginUser = new CasLoginUser();

            casLoginUser.setLoginUser(loginUser);
            return casLoginUser;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
}