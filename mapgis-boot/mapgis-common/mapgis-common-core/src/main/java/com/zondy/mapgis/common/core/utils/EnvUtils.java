package com.zondy.mapgis.common.core.utils;

import cn.hutool.setting.dialect.Props;
import com.sun.management.OperatingSystemMXBean;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * @author xiongbo
 * @since 2022/3/15 18:00
 */
@Slf4j
public class EnvUtils {
    /**
     * 注意静态变量和静态代码块的执行次序
     */
    /**
     * 操作系统：Windows
     */
    public static final boolean IS_OS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("windows");

    /**
     * 操作系统：Linux
     */
    public static final boolean IS_OS_LINUX = System.getProperty("os.name").toLowerCase().contains("linux");

    /**
     * 系统CPU架构：amd64/x86_64
     */
    public static final boolean IS_AMD64_LINUX = System.getProperty("os.arch").toLowerCase().contains("amd64");

    /**
     * 系统CPU架构：arm64
     */
    public static final boolean IS_ARM64_LINUX = System.getProperty("os.arch").toLowerCase().contains("aarch64");

    /**
     * 系统CPU架构：sw64（国产申威）
     */
    public static final boolean IS_SHENWEI64_LINUX = System.getProperty("os.arch").toLowerCase().contains("sw64");

    /**
     * 系统CPU架构：mips64el（国产龙芯）
     */
    public static final boolean IS_LONGXIN_LINUX = System.getProperty("os.arch").toLowerCase().contains("mips64el");

    /**
     * 调试时，在配置文件中添加配置开启调试日志'logging.level.DEBUG_LOG=debug'，默认关闭
     */
    public static final Logger LOGGER = LoggerFactory.getLogger("DEBUG_LOG");

    /**
     * 控制台输出被禁用属性名
     */
    public static final String CONSOLE_OUT_DISABLED_PROP = "ConsoleOutDisabled";

    /**
     * 控制台输出是否被禁用
     */
    public static final boolean CONSOLE_OUT_DISABLED = "true".equals(System.getProperty(CONSOLE_OUT_DISABLED_PROP));

    /**
     * 是否开启Nacos微服务注册中心
     */
    public static final String NACOS_DISCOVERY_ENABLE = "spring.cloud.nacos.discovery.enabled";

    /**
     * Nacos微服务注册中心地址
     */
    public static final String NACOS_DISCOVERY_SERVER_ADDR = "spring.cloud.nacos.discovery.server-addr";

    /**
     * 获取当前项目路径
     */
    public static String getCurrentProjectPath() {
        return System.getProperty("user.dir");
    }

    /**
     * 获取系统的总物理内存
     *
     * @return 内存量，单位M
     */
    public static int getTotalPhysicalMemory() {
        OperatingSystemMXBean osMxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        return (int) (osMxb.getTotalPhysicalMemorySize() / 1024 / 1024);
    }

    public static String createMultiLineLogs(String... lines) {
        if (lines != null && lines.length > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n=============================================================");
            for (String line : lines) {
                sb.append("\n     ");
                sb.append(line);
            }
            sb.append("\n=============================================================");
            return sb.toString();
        }
        return "";
    }

    /**
     * 是否开启了集群
     *
     * @param env 环境bean对象
     * @return 启用集群状态
     */
    public static boolean enableCluster(Environment env) {
        return "true".equals(env.getProperty(NACOS_DISCOVERY_ENABLE));
    }

    /**
     * 是否开启了Nacos微服务注册中心
     *
     * @param env 环境bean对象
     * @return Nacos开启状态
     */
    public static boolean nacosEnabled(Environment env) {
        String nacosEnabled = env.getProperty(NACOS_DISCOVERY_ENABLE);
        String nacosServerAddr = env.getProperty(NACOS_DISCOVERY_SERVER_ADDR);

        return (nacosEnabled == null || "true".equals(nacosEnabled)) && StringUtils.isNotBlank(nacosServerAddr);
    }

    /**
     * 是否是单体版模式
     *
     * @param env 环境对象
     * @return 结果
     */
    public static boolean isSingleServiceMode(Environment env) {
        return "ehcache".equals(env.getProperty("spring.cache.type"));
    }

    /**
     * 获取指定的环境变量集合
     *
     * @param envNames
     * @return
     */
    public static Map<String, String> getEnvParams(String[] envNames) {
        Map<String, String> map = new HashMap<>();
        if (envNames == null) {
            return map;
        }

        for (String envName : envNames) {
            if (envName != null && System.getenv(envName) != null) {
                map.put(envName, System.getenv(envName));
            }
        }
        return map;
    }

    /**
     * 设置环境变量
     *
     * @param key
     * @param value
     */
    public static void setEnv(final String key, final String value) {
        try {
            /// we obtain the actual environment
            final Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            final Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            final boolean environmentAccessibility = theEnvironmentField.isAccessible();
            theEnvironmentField.setAccessible(true);

            final Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);

            if (IS_OS_WINDOWS) {
                // This is all that is needed on windows running java jdk 1.8.0_92
                if (value == null) {
                    env.remove(key);
                } else {
                    env.put(key, value);
                }
            } else {
                // This is triggered to work on openjdk 1.8.0_91
                // The ProcessEnvironment$Variable is the key of the map
                final Class<String> variableClass = (Class<String>) Class.forName("java.lang.ProcessEnvironment$Variable");
                final Method convertToVariable = variableClass.getMethod("valueOf", String.class);
                final boolean conversionVariableAccessibility = convertToVariable.isAccessible();
                convertToVariable.setAccessible(true);

                // The ProcessEnvironment$Value is the value fo the map
                final Class<String> valueClass = (Class<String>) Class.forName("java.lang.ProcessEnvironment$Value");
                final Method convertToValue = valueClass.getMethod("valueOf", String.class);
                final boolean conversionValueAccessibility = convertToValue.isAccessible();
                convertToValue.setAccessible(true);

                if (value == null) {
                    env.remove(convertToVariable.invoke(null, key));
                } else {
                    // we place the new value inside the map after conversion so as to
                    // avoid class cast exceptions when rerunning this code
                    env.put((String) convertToVariable.invoke(null, key), (String) convertToValue.invoke(null, value));

                    // reset accessibility to what they were
                    convertToValue.setAccessible(conversionValueAccessibility);
                    convertToVariable.setAccessible(conversionVariableAccessibility);
                }
            }
            // reset environment accessibility
            theEnvironmentField.setAccessible(environmentAccessibility);

            // we apply the same to the case insensitive environment
            final Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
            final boolean insensitiveAccessibility = theCaseInsensitiveEnvironmentField.isAccessible();
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            // Not entirely sure if this needs to be casted to ProcessEnvironment$Variable and $Value as well
            final Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
            if (value == null) {
                // remove if null
                cienv.remove(key);
            } else {
                cienv.put(key, value);
            }
            theCaseInsensitiveEnvironmentField.setAccessible(insensitiveAccessibility);
        } catch (final ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Failed setting environment variable <" + key + "> to <" + value + ">", e);
        } catch (final NoSuchFieldException e) {
            // we could not find theEnvironment
            final Map<String, String> env = System.getenv();
            Stream.of(Collections.class.getDeclaredClasses())
                    // obtain the declared classes of type $UnmodifiableMap
                    .filter(c1 -> "java.util.Collections$UnmodifiableMap".equals(c1.getName())).map(c1 -> {
                        try {
                            return c1.getDeclaredField("m");
                        } catch (final NoSuchFieldException e1) {
                            throw new IllegalStateException("Failed setting environment variable <" + key + "> to <" + value + "> when locating in-class memory map of environment", e1);
                        }
                    }).forEach(field -> {
                        try {
                            final boolean fieldAccessibility = field.isAccessible();
                            field.setAccessible(true);
                            // we obtain the environment
                            final Map<String, String> map = (Map<String, String>) field.get(env);
                            if (value == null) {
                                // remove if null
                                map.remove(key);
                            } else {
                                map.put(key, value);
                            }
                            // reset accessibility
                            field.setAccessible(fieldAccessibility);
                        } catch (final ConcurrentModificationException e1) {
                            // This may happen if we keep backups of the environment before calling this method
                            // as the map that we kept as a backup may be picked up inside this block.
                            // So we simply skip this attempt and continue adjusting the other maps
                            // To avoid this one should always keep individual keys/value backups not the entire map
                            log.info("Attempted to modify source map: " + field.getDeclaringClass() + "#" + field.getName(), e1);
                        } catch (final IllegalAccessException e1) {
                            throw new IllegalStateException("Failed setting environment variable <" + key + "> to <" + value + ">. Unable to access field!", e1);
                        }
                    });
        }
        log.info("Set environment variable <" + key + "> to <" + value + ">. Sanity Check: " + System.getenv(key));
    }

    /**
     * 获取版本
     *
     * @param versionCls
     * @return
     */
    public static String getVersion(Class<?> versionCls) {
        return determineSpringBootVersion(versionCls);
    }

    private static String determineSpringBootVersion(Class<?> cls) {
        String implementationVersion = cls.getPackage().getImplementationVersion();
        if (implementationVersion != null) {
            return implementationVersion;
        } else {
            CodeSource codeSource = cls.getProtectionDomain().getCodeSource();
            if (codeSource == null) {
                return null;
            } else {
                URL codeSourceLocation = codeSource.getLocation();

                try {
                    URLConnection connection = codeSourceLocation.openConnection();
                    if (connection instanceof JarURLConnection) {
                        return getImplementationVersion(((JarURLConnection) connection).getJarFile());
                    } else {
                        // 获取调试状态下项目版本号
                        String fileName = connection.getURL().getFile();
                        Path pomProperties = Paths.get(fileName.substring(1, fileName.length() - 2), "..", "maven-archiver", "pom.properties").normalize().toAbsolutePath();
                        if (pomProperties.toFile().exists()) {
                            Props props = new Props(pomProperties.toString());
                            return props.getProperty("version");
                        }

                        JarFile jarFile = new JarFile(new File(codeSourceLocation.toURI()));
                        Throwable var5 = null;

                        String var6;
                        try {
                            var6 = getImplementationVersion(jarFile);
                        } catch (Throwable var16) {
                            var5 = var16;
                            throw var16;
                        } finally {
                            if (jarFile != null) {
                                if (var5 != null) {
                                    try {
                                        jarFile.close();
                                    } catch (Throwable var15) {
                                        var5.addSuppressed(var15);
                                    }
                                } else {
                                    jarFile.close();
                                }
                            }
                        }
                        return var6;
                    }
                } catch (Exception var18) {
                    return null;
                }
            }
        }
    }

    private static String getImplementationVersion(JarFile jarFile) throws IOException {
        return jarFile.getManifest().getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VERSION);
    }

    /**
     * 指定目录中根据jar包前缀查询jar文件
     *
     * @param dir           目录
     * @param jarFilePrefix 除去版本和后缀名的文件前缀，注意以"-"结尾
     * @return jar文件名称
     * @throws IOException 文件查找异常
     */
    public static String getJarFileName(File dir, String jarFilePrefix) throws IOException {
        try (Stream<Path> stream = Files.find(dir.toPath(), 1, (path, attr) -> {
            String name = path.toFile().getName();
            // 只能为 jarFilePrefix-1.0.0.1.jar格式
            return name.startsWith(jarFilePrefix) && name.substring(jarFilePrefix.length()).matches("^[0-9.]*\\.jar$");
        })) {
            // 保证多个版本同时存在时，会取最大的版本号
            return stream.map(t -> t.toFile().getName()).max(Comparator.naturalOrder()).orElse(null);
        }
    }

    public static void debug(String var1) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(var1);
        }
    }

    public static void debug(String var1, Object... var2) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(var1, var2);
        }
    }
}
