package com.zondy.mapgis.system.api.domain;

import lombok.Data;

/**
 * CAS登录配置对象
 *
 * @author xiongbo
 * @since 2022/11/11 13:39
 */
@Data
public class SysCasConfig {
    /**
     * 是否启用
     */
    Boolean enabled;

    /**
     * 是否保留默认登录方式
     */
    Boolean isReserveDefaultLogin;

    /**
     * CAS服务器访问地址
     */
    String casServerUrl;

    /**
     * CAS客户端主机地址
     */
    String casServiceHostUrl;

    /**
     * CAS客户端Web前端地址
     */
    String casServiceWebUrl;

    /**
     * REST服务前缀
     */
    String servicesPrefix;

    public SysCasConfig() {
        enabled = false;
        isReserveDefaultLogin = false;
        casServerUrl = "";
        casServiceHostUrl = "";
        casServiceWebUrl = "";
        servicesPrefix = "";
    }

    /**
     * 获取CAS登录地址
     *
     * @return
     */
    public String getCasServerLoginUrl() {
        return casServerUrl + "/login";
    }

    /**
     * 获取CAS退出地址
     *
     * @return
     */
    public String getCasServerLogoutUrl() {
        return casServerUrl + "/logout";
    }

    /**
     * 获取CAS客户端登录路径
     *
     * @return CAS客户端登录路径
     */
    public String getCasServiceLoginPath() {
        return servicesPrefix + "/auth/casLogin/login";
    }

    /**
     * 获取客户端登录地址
     *
     * @return 客户端登录地址
     */
    public String getCasServiceLoginUrl() {
        return casServiceHostUrl + getCasServiceLoginPath();
    }

    /**
     * 获取CAS登录完整地址
     *
     * @return CAS登录完整地址
     */
    public String getCasLoginUrl() {
        return getCasServerLoginUrl() + "?service=" + getCasServiceLoginUrl();
    }

    /**
     * 获取CAS退出完整地址
     *
     * @return CAS退出完整地址
     */
    public String getCasLogoutUrl() {
        return getCasServerLogoutUrl() + "?service=" + casServiceWebUrl;
    }
}
