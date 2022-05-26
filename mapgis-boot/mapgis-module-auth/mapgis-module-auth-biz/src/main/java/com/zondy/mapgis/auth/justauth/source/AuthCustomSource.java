package com.zondy.mapgis.auth.justauth.source;

import com.zondy.mapgis.auth.justauth.config.properties.AuthCustomProperties;
import com.zondy.mapgis.common.core.utils.spring.SpringUtils;
import me.zhyd.oauth.config.AuthSource;

/**
 * 自定义 source
 *
 * @author xiongbo
 * @since 2022/5/25 19:15
 */
public enum AuthCustomSource implements AuthSource {
    /**
     * 自定义
     */
    CUSTOM {
        /**
         * 授权的api
         *
         * @return url
         */
        @Override
        public String authorize() {
            return SpringUtils.getBean(AuthCustomProperties.class).getAuthorize();
        }

        /**
         * 获取accessToken的api
         *
         * @return url
         */
        @Override
        public String accessToken() {
            return SpringUtils.getBean(AuthCustomProperties.class).getAccessToken();
        }

        /**
         * 获取用户信息的api
         *
         * @return url
         */
        @Override
        public String userInfo() {
            return SpringUtils.getBean(AuthCustomProperties.class).getUserInfo();
        }
    }
}
