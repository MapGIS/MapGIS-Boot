package com.zondy.mapgis.system.api.factory;

import com.zondy.mapgis.common.core.domain.R;
import com.zondy.mapgis.system.api.ISysServiceApi;
import com.zondy.mapgis.system.api.domain.SysLogininfor;
import com.zondy.mapgis.system.api.domain.SysOperLog;
import com.zondy.mapgis.system.api.domain.SysUser;
import com.zondy.mapgis.system.api.model.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 系统基础服务降级处理
 *
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Component
public class RemoteSysServiceApiFallbackFactory implements FallbackFactory<ISysServiceApi> {
    private static final Logger log = LoggerFactory.getLogger(RemoteSysServiceApiFallbackFactory.class);

    @Override
    public ISysServiceApi create(Throwable throwable) {
        log.error("系统基础服务调用失败:{}", throwable.getMessage());
        return new ISysServiceApi() {
            @Override
            public R<LoginUser> getUserInfo(String username, String source) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> registerUserInfo(SysUser sysUser, String source) {
                return R.fail("注册用户失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> saveLog(SysOperLog sysOperLog, String source) {
                return R.fail("添加操作日志失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> saveLogininfor(SysLogininfor sysLogininfor, String source) {
                return R.fail("添加访问日志失败:" + throwable.getMessage());
            }
        };
    }
}
