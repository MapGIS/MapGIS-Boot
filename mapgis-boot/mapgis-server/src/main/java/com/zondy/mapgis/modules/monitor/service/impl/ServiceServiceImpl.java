package com.zondy.mapgis.modules.monitor.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import com.zondy.mapgis.common.core.utils.DateUtils;
import com.zondy.mapgis.common.core.utils.ip.IpUtils;
import com.zondy.mapgis.common.core.utils.network.NetworkUtils;
import com.zondy.mapgis.modules.monitor.service.ServerService;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.VirtualMemory;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author xiongbo
 * @since 2022/4/24 17:35
 */
@Service
public class ServiceServiceImpl implements ServerService {

    private final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public Map<String, Object> getServers() {
        Map<String, Object> resultMap = new LinkedHashMap<>(8);
        try {
            SystemInfo si = new SystemInfo();
            OperatingSystem os = si.getOperatingSystem();
            HardwareAbstractionLayer hal = si.getHardware();
            // 系统信息
            resultMap.put("sys", getSystemInfo(os));
            // cpu 信息
            resultMap.put("cpu", getCpuInfo(hal.getProcessor()));
            // 内存信息
            resultMap.put("memory", getMemoryInfo(hal.getMemory()));
            // 交换区信息
            resultMap.put("swap", getSwapInfo(hal.getMemory()));
            // 磁盘
            resultMap.put("disk", getDiskInfo(os));
            // 网络
            resultMap.put("network", getNetworkInfo());
            // JVM
            resultMap.put("jvm", getJvmInfo());
            // 文件系统
            resultMap.put("files", getFilesInfo(os));
            resultMap.put("time", DateUtil.format(new Date(), "HH:mm:ss"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 获取磁盘信息
     *
     * @return /
     */
    private Map<String, Object> getDiskInfo(OperatingSystem os) {
        Map<String, Object> diskInfo = new LinkedHashMap<>();
        FileSystem fileSystem = os.getFileSystem();
        AtomicLong storageTotal = new AtomicLong();
        AtomicLong storageUsed = new AtomicLong();
        AtomicLong storageFree = new AtomicLong();
        fileSystem.getFileStores().forEach(osFileStore -> {
            long totalSpace = osFileStore.getTotalSpace();
            long usableSpace = osFileStore.getUsableSpace();
            long freeSpace = osFileStore.getFreeSpace();
            long usedSpace = totalSpace - usableSpace;
            storageTotal.addAndGet(totalSpace);
            storageUsed.addAndGet(usedSpace);
            storageFree.addAndGet(freeSpace);
        });

        diskInfo.put("total", FileUtil.readableFileSize(storageTotal.get()));
        diskInfo.put("used", FileUtil.readableFileSize(storageUsed.get()));
        diskInfo.put("free", FileUtil.readableFileSize(storageFree.get()));
        diskInfo.put("usage", NumberUtil.mul(NumberUtil.div(storageUsed.doubleValue(), storageTotal.doubleValue(), 4), 100));

        return diskInfo;
    }

    /**
     * 获取交换区信息
     *
     * @param memory /
     * @return /
     */
    private Map<String, Object> getSwapInfo(GlobalMemory memory) {
        Map<String, Object> swapInfo = new LinkedHashMap<>();
        VirtualMemory virtualMemory = memory.getVirtualMemory();
        long total = virtualMemory.getSwapTotal();
        long used = virtualMemory.getSwapUsed();
        swapInfo.put("total", FileUtil.readableFileSize(total));
        swapInfo.put("used", FileUtil.readableFileSize(used));
        swapInfo.put("free", FileUtil.readableFileSize(total - used));
        if (used == 0) {
            swapInfo.put("usage", 0);
        } else {
            swapInfo.put("usage", NumberUtil.mul(NumberUtil.div(used, total, 4), 100));
        }
        return swapInfo;
    }

    /**
     * 获取内存信息
     *
     * @param memory /
     * @return /
     */
    private Map<String, Object> getMemoryInfo(GlobalMemory memory) {
        Map<String, Object> memoryInfo = new LinkedHashMap<>();
        memoryInfo.put("total", FileUtil.readableFileSize(memory.getTotal()));
        memoryInfo.put("used", FileUtil.readableFileSize(memory.getTotal() - memory.getAvailable()));
        memoryInfo.put("free", FileUtil.readableFileSize(memory.getAvailable()));
        memoryInfo.put("usage", NumberUtil.mul(NumberUtil.div(memory.getTotal() - memory.getAvailable(), memory.getTotal(), 4), 100));
        return memoryInfo;
    }

    /**
     * 获取Cpu相关信息
     *
     * @param processor /
     * @return /
     */
    private Map<String, Object> getCpuInfo(CentralProcessor processor) {
        Map<String, Object> cpuInfo = new LinkedHashMap<>();
        cpuInfo.put("name", processor.getProcessorIdentifier().getName());
        cpuInfo.put("package", processor.getPhysicalPackageCount() + "颗物理CPU");
        cpuInfo.put("physical", processor.getPhysicalProcessorCount() + "个物理核心");
        cpuInfo.put("logical", processor.getLogicalProcessorCount() + "个逻辑核心");
        // CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        // 等待1秒...
        Util.sleep(1000);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long sys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;
        cpuInfo.put("sys", NumberUtil.div(NumberUtil.mul(sys, 100), totalCpu, 2));
        cpuInfo.put("user", NumberUtil.div(NumberUtil.mul(user, 100), totalCpu, 2));
        cpuInfo.put("total", NumberUtil.div(NumberUtil.mul(NumberUtil.add(sys, user), 100), totalCpu, 2));
        cpuInfo.put("wait", NumberUtil.div(NumberUtil.mul(iowait, 100), totalCpu, 2));
        cpuInfo.put("free", NumberUtil.div(NumberUtil.mul(idle, 100), totalCpu, 2));

        return cpuInfo;
    }

    /**
     * 获取系统相关信息,系统名称、操作系统、IP、架构、项目路径、系统完整信息
     *
     * @param os /
     * @return /
     */
    private Map<String, Object> getSystemInfo(OperatingSystem os) {
        Properties props = System.getProperties();
        Map<String, Object> systemInfo = new LinkedHashMap<>();

        // 系统信息
        systemInfo.put("name", IpUtils.getHostName());
        systemInfo.put("os", props.getProperty("os.name"));
        systemInfo.put("ip", IpUtils.getHostIp());
        systemInfo.put("arch", props.getProperty("os.arch"));
        systemInfo.put("userDir", props.getProperty("user.dir"));
        systemInfo.put("osFullInfo", os.toString());

        return systemInfo;
    }

    /**
     * 获取网络速率
     *
     * @return
     */
    private Map<String, Object> getNetworkInfo() {
        Map<String, Object> networkInfo = new LinkedHashMap<>();
        Map<String, String> networkUpRate = NetworkUtils.getNetworkUpRate();

        networkInfo.put("uplink", networkUpRate.get("UP"));
        networkInfo.put("downlink", networkUpRate.get("DOWN"));
        return networkInfo;
    }

    /**
     * 获取JVM信息
     *
     * @return
     */
    private Map<String, Object> getJvmInfo() {
        Map<String, Object> jvmInfo = new LinkedHashMap<>();

        Properties props = System.getProperties();

        jvmInfo.put("name", ManagementFactory.getRuntimeMXBean().getVmName());
        jvmInfo.put("version", ManagementFactory.getRuntimeMXBean().getVmVersion());
        jvmInfo.put("startTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, DateUtils.getServerStartDate()));
        jvmInfo.put("runTime", DateUtils.getDatePoor(DateUtils.getNowDate(), DateUtils.getServerStartDate()));
        jvmInfo.put("javaVersion", props.getProperty("java.version"));
        jvmInfo.put("javaPath", props.getProperty("java.home"));
        jvmInfo.put("inputArgs", ManagementFactory.getRuntimeMXBean().getInputArguments().toString());
        jvmInfo.put("max", FileUtil.readableFileSize(Runtime.getRuntime().maxMemory()));
        long totalMemory = Runtime.getRuntime().totalMemory();
        jvmInfo.put("total", FileUtil.readableFileSize(totalMemory));
        long jvmMemoryUsed = NumberUtil.sub(new BigDecimal(Runtime.getRuntime().totalMemory()), new BigDecimal(Runtime.getRuntime().freeMemory())).longValue();
        jvmInfo.put("used", FileUtil.readableFileSize(jvmMemoryUsed));
        jvmInfo.put("free", FileUtil.readableFileSize(Runtime.getRuntime().freeMemory()));
        jvmInfo.put("usage", NumberUtil.mul(NumberUtil.div(jvmMemoryUsed, totalMemory, 4), 100));
        return jvmInfo;
    }

    /**
     * 获取磁盘状态
     */
    private List<Map<String, Object>> getFilesInfo(OperatingSystem os) {
        List<Map<String, Object>> filesInfo = new ArrayList<>();

        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
            Map<String, Object> fileInfo = new LinkedHashMap<>();

            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;

            fileInfo.put("dir", fs.getMount());
            fileInfo.put("type", fs.getType());
            fileInfo.put("name", fs.getName());

            fileInfo.put("total", FileUtil.readableFileSize(total));
            fileInfo.put("used", FileUtil.readableFileSize(used));
            fileInfo.put("free", FileUtil.readableFileSize(free));
            fileInfo.put("usage", NumberUtil.mul(NumberUtil.div(used, total, 4), 100));

            filesInfo.add(fileInfo);
        }

        return filesInfo;
    }
}
