package com.zondy.mapgis.common.core.platform;

import com.zondy.mapgis.common.core.utils.EnvUtils;

/**
 * 平台版本
 *
 * @author xiongbo
 * @since 2023/1/10 9:48
 */
public class PlatformVersion {
    private PlatformVersion() {
    }

    public static String getVersion() {
        String version = EnvUtils.getVersion(PlatformVersion.class);
        if (version == null) {
            return "1.0.0.0";
        }
        return version;
    }
}