package com.zondy.mapgis.system.api.factory;

import com.zondy.mapgis.common.core.domain.R;
import com.zondy.mapgis.system.api.ISysServiceApi;
import com.zondy.mapgis.system.api.domain.*;
import com.zondy.mapgis.system.api.model.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

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
            public R<Boolean> updateUserProfile(SysUser sysUser, String source) {
                return R.fail("更新用户失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> saveLog(SysOperLog sysOperLog, String source) {
                return R.fail("添加操作日志失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> saveLogininfor(SysLogininfor sysLogininfor, String source) {
                return R.fail("添加访问日志失败:" + throwable.getMessage());
            }

            @Override
            public R<List<SysAuthUser>> selectAuthUserList(SysAuthUser user, String source) {
                return R.fail("获取第三方授权用户失败:" + throwable.getMessage());
            }

            @Override
            public R<SysUser> selectUserByAuthUuid(String uuid, String source) {
                return R.fail("根据第三方授权UUID查询用户:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> saveAuthUser(SysAuthUser authUser, String source) {
                return R.fail("添加第三方授权用户失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> updateAuthUser(SysAuthUser authUser, String source) {
                return R.fail("更新第三方授权用户失败:" + throwable.getMessage());
            }

            @Override
            public R<String> selectConfigValueByKey(String configKey, String source) {
                return R.fail("根据键名查询参数配置信息失败:" + throwable.getMessage());
            }

            @Override
            public R<SysAuthConfig> selectAuthConfigByType(String type, String source) {
                return R.fail("获取第三方登录配置失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> savePerformanceMonitorRecord(SysServerPerformanceData sysServerPerformanceData, String source) {
                return R.fail("保存服务性能数据失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> saveAccessLog(SysHttpAccess httpAccess, String source) {
                return R.fail("保存HTTP访问日志失败:" + throwable.getMessage());
            }

            @Override
            public R<List<Long>> selectRolesByUserId(Long userId, String source) {
                return R.fail("获取指定用户的角色列表失败:" + throwable.getMessage());
            }
        };
    }
}
