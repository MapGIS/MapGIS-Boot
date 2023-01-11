package com.zondy.mapgis.common.service.id;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * 服务唯一标识
 * 由于现在服务的标识包括服务名和服务文件夹，取代以前的serviceName参数，也为了方便后期扩展
 *
 * @author Chelsea
 * @since 2023/1/10 13:51
 */
@EqualsAndHashCode
public abstract class ServiceId {
    /**
     * 服务名称
     *
     * @return 服务名
     */
    public abstract String getName();

    /**
     * 服务文件夹名
     *
     * @return 文件夹
     */
    public abstract String getFolder();

    /**
     * 返回组合的服务名
     *
     * @return [folder:]name
     */
    public abstract String toCompositeName();

    /**
     * 返回ogc组合的服务名
     * 因为ogc服务名不能带':',这里使用'_'来分隔文件夹和服务名
     *
     * @return [folder_]name
     */
    public abstract String toOgcServerName();

    /**
     * 复合服务名
     *
     * @param compositeName [folder/]name 或 [folder:]name 形式
     * @return 服务id
     */
    public static ServiceId fromCompositeName(@NonNull String compositeName) {
        return new DefaultServiceId(compositeName);
    }

    public static ServiceId fromServicePubItem(@NonNull String serviceName, String folder) {
        return new DefaultServiceId(serviceName, folder);
    }

    public static ServiceId from(@NonNull String name, String folder) {
        return new DefaultServiceId(name, folder);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    private static class DefaultServiceId extends ServiceId {
        /**
         * 服务的名或服务目录路径
         */
        private String name;
        private String folder = "";

        public DefaultServiceId(@NonNull String name, String folder) {
            this.name = name;
            this.folder = folder == null ? "" : folder;
        }

        /**
         * 支持复杂形式的服务名
         *
         * @param nameWithFolder [folder/]name 或 [folder:]name 形式
         */
        public DefaultServiceId(String nameWithFolder) {
            if (nameWithFolder != null) {
                if (nameWithFolder.contains("/")) {
                    this.folder = nameWithFolder.split("/")[0];
                    this.name = nameWithFolder.split("/")[1];
                } else if (nameWithFolder.contains(":")) {
                    this.folder = nameWithFolder.split(":")[0];
                    this.name = nameWithFolder.split(":")[1];
                } else {
                    this.name = nameWithFolder;
                }
            }
        }

        @Override
        public String toString() {
            return toCompositeName();
        }

        @Override
        public String toCompositeName() {
            return toName(":");
        }

        @Override
        public String toOgcServerName() {
            return toName("_");
        }

        private String toName(String split) {
            return ((folder != null && folder.length() > 0) ? (folder + split) : "") + name;
        }
    }
}