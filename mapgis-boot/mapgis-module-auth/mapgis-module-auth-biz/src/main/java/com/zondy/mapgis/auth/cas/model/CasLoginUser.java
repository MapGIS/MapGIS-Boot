package com.zondy.mapgis.auth.cas.model;

import com.zondy.mapgis.system.api.model.LoginUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

/**
 * CasLoginUser用户统一单体和微服务版的LoginUser
 *
 * @author powanjuanshu
 * @since 2022/6/10 12:47
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CasLoginUser implements UserDetails {
    private static final long serialVersionUID = 1L;

    private LoginUser loginUser;

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return loginUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet();
    }
}
