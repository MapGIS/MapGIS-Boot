package com.zondy.mapgis.auth.api.service.impl;

import com.zondy.mapgis.auth.api.service.ILdapService;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import javax.naming.directory.DirContext;
import java.util.List;
import java.util.Map;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

/**
 * @author powanjuanshu
 * @since 2022/11/3 17:31
 */
@Service
public class LdapServiceImpl implements ILdapService {
    @Override
    public boolean authenticate(Map<String, Object> ldapConfig, String username, String password) {
        String userDn = "";
        DirContext ctx = null;
        LdapTemplate ldapTemplate = getLdapTemplate(ldapConfig);

        try {
            userDn = getUserDn(ldapTemplate, username);
            ctx = ldapTemplate.getContextSource().getContext(userDn, password);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            LdapUtils.closeContext(ctx);
        }
    }

    @Override
    public List<String> getUserGroups(Map<String, Object> ldapConfig, String username) {
        LdapTemplate ldapTemplate = getLdapTemplate(ldapConfig);
        String filter = "(|(&(objectClass=groupOfNames)(member=" + username + "*))(&(objectClass=groupOfUniqueNames)(uniqueMember=" + username + "*))(&(objectClass=posixGroup)(memberUid=*" + username + "*)))";

        return ldapTemplate.search("", filter, (AttributesMapper<String>) attrs -> (String) attrs.get("cn").get());
    }

    @Override
    public List<String> getAllGroups(Map<String, Object> ldapConfig) {
        LdapTemplate ldapTemplate = getLdapTemplate(ldapConfig);
        String str = "(|(objectClass=groupOfNames)(objectClass=groupOfUniqueNames)(objectClass=posixGroup))";

        return ldapTemplate.search("", str, (AttributesMapper<String>) attrs -> (String) attrs.get("cn").get());
    }

    private LdapTemplate getLdapTemplate(Map<String, Object> ldapConfig) {
        LdapContextSource ldapContextSource = new LdapContextSource();

        ldapContextSource.setUrl((String) ldapConfig.get("url"));
        ldapContextSource.setBase((String) ldapConfig.get("base"));
        ldapContextSource.setUserDn((String) ldapConfig.get("userDn"));
        ldapContextSource.setPassword((String) ldapConfig.get("password"));
        ldapContextSource.afterPropertiesSet();

        return new LdapTemplate(ldapContextSource);
    }

    private String getUserDn(LdapTemplate ldapTemplate, String username) {
        List<String> userDns = null;

        try {
            userDns = ldapTemplate.search(query().where("uid").is(username), new AbstractContextMapper<String>() {
                @Override
                protected String doMapFromContext(DirContextOperations ctx) {
                    return ctx.getNameInNamespace();
                }
            });
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
        if (userDns.size() != 1) {
            throw new RuntimeException("账号不是唯一");
        }
        return userDns.get(0);
    }
}
