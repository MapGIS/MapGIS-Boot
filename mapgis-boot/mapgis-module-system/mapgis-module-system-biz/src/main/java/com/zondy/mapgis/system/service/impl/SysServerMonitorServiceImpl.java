package com.zondy.mapgis.system.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.zondy.mapgis.common.core.utils.DateUtils;
import com.zondy.mapgis.common.core.utils.EnvUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.core.utils.ip.IpUtils;
import com.zondy.mapgis.system.domain.SysCpu;
import com.zondy.mapgis.system.domain.SysDisk;
import com.zondy.mapgis.system.domain.SysMemory;
import com.zondy.mapgis.system.domain.SysNetwork;
import com.zondy.mapgis.system.mapper.SysCpuMapper;
import com.zondy.mapgis.system.mapper.SysDiskMapper;
import com.zondy.mapgis.system.mapper.SysMemoryMapper;
import com.zondy.mapgis.system.mapper.SysNetworkMapper;
import com.zondy.mapgis.system.service.ISysServerMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * CPU信息Service业务层处理
 *
 * @author mapgis
 * @date 2023-01-03
 */
@Service
@DS("hardware_monitor")
public class SysServerMonitorServiceImpl implements ISysServerMonitorService {
    @Autowired
    private SysCpuMapper sysCpuMapper;

    @Autowired
    private SysMemoryMapper sysMemoryMapper;

    @Autowired
    private SysDiskMapper sysDiskMapper;

    @Autowired
    private SysNetworkMapper sysNetworkMapper;

    private final int MAX_ITEM = 360;

    /**
     * 上一次cpu的占用情况
     */
    private long[] prevTicks = null;

    @Override
    public Map<String, Object> getMonitorInfo() {
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
            // 磁盘
            resultMap.put("disk", getDiskInfo(os));
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

    @Override
    public Map<String, Object> getMonitorInfoBetweenTime(String beginTime, String endTime) {
        Map<String, Object> timeParam = new HashMap<>();
        timeParam.put("beginTime", beginTime);
        timeParam.put("endTime", endTime);

        Map<String, Object> monitorInfo = new HashMap<>();

        final List<Map<String, Object>> persistentCpuInfo = getPersistentCpuInfo(timeParam);
        final List<Map<String, Object>> persistentMemoryInfo = getPersistentMemoryInfo(timeParam);
        final List<Map<String, Object>> persistentDiskReadInfo = getPersistentDiskInfo(0, timeParam);
        final List<Map<String, Object>> persistentDiskWriteInfo = getPersistentDiskInfo(1, timeParam);
        final List<Map<String, Object>> persistentNetworkUpInfo = getPersistentNetworkInfo(0, timeParam);
        final List<Map<String, Object>> persistentNetworkDownInfo = getPersistentNetworkInfo(1, timeParam);

        // cpu 信息
        monitorInfo.put("cpu", persistentCpuInfo);
        // 内存信息
        monitorInfo.put("memory", persistentMemoryInfo);
        // 磁盘
        monitorInfo.put("diskRead", persistentDiskReadInfo);
        monitorInfo.put("diskWrite", persistentDiskWriteInfo);
        // 网络
        monitorInfo.put("networkUp", persistentNetworkUpInfo);
        monitorInfo.put("networkDown", persistentNetworkDownInfo);
        return monitorInfo;
    }

    /**
     * 监控本机的硬件资源CPU/内存/网络/磁盘 1分钟记录一次
     */
    @Scheduled(initialDelay = 5 * DateUtils.MILLIS_PER_SECOND, fixedDelay = 60 * DateUtils.MILLIS_PER_SECOND)
    public void dealHardwareMonitor() {
        // oshi不支持申威
        if (EnvUtils.IS_SHENWEI64_LINUX) {
            return;
        }

        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        Date time = DateUtils.getNowDate();

        // cpu 信息
        SysCpu sysCpu = new SysCpu();
        Map<String, Object> cpuInfo = getCpuInfo(hal.getProcessor());
        sysCpu.setPercent((Double) cpuInfo.get("total"));
        sysCpu.setTime(time);

        // 内存信息
        SysMemory sysMemory = new SysMemory();
        Map<String, Object> memoryInfo = getMemoryInfo(hal.getMemory());
        sysMemory.setPercent((Double) memoryInfo.get("usage"));
        sysMemory.setTime(time);

        // 磁盘
        SysDisk sysDiskRead = new SysDisk();
        SysDisk sysDiskWrite = new SysDisk();
        long[] diskReadwrite = getDiskReadwrite(hal);

        sysDiskRead.setSpeed((double) (diskReadwrite[0] / 1024));
        sysDiskRead.setReadwrite(0);
        sysDiskRead.setTime(time);

        sysDiskWrite.setSpeed((double) (diskReadwrite[1] / 1024));
        sysDiskWrite.setReadwrite(1);
        sysDiskWrite.setTime(time);

        // 网络
        SysNetwork sysNetworkUp = new SysNetwork();
        SysNetwork sysNetworkDown = new SysNetwork();
        long[] networkUpdown = getNetworkUpdown(hal);

        sysNetworkUp.setSpeed((double) (networkUpdown[0] / 1024));
        sysNetworkUp.setUpdown(0);
        sysNetworkUp.setTime(time);

        sysNetworkDown.setSpeed((double) (networkUpdown[1] / 1024));
        sysNetworkDown.setUpdown(1);
        sysNetworkDown.setTime(time);

        // 持久化
        sysCpuMapper.insertSysCpu(sysCpu);
        sysMemoryMapper.insertSysMemory(sysMemory);
        sysDiskMapper.insertSysDisk(sysDiskRead);
        sysDiskMapper.insertSysDisk(sysDiskWrite);
        sysNetworkMapper.insertSysNetwork(sysNetworkUp);
        sysNetworkMapper.insertSysNetwork(sysNetworkDown);
    }

    /**
     * 每天清理一下监控记录，超过90天的将被清除
     */
    @Scheduled(initialDelay = 5 * DateUtils.MILLIS_PER_SECOND, fixedDelay = DateUtils.MILLIS_PER_DAY)
    public void clearGarbage() {
        sysCpuMapper.clearExpired();
        sysMemoryMapper.clearExpired();
        sysDiskMapper.clearExpired();
        sysNetworkMapper.clearExpired();
    }

    private List<Map<String, Object>> getPersistentCpuInfo(Map<String, Object> timeParam) {
        SysCpu queryParam = new SysCpu();
        queryParam.setParams(timeParam);

        List<SysCpu> cpuList = sysCpuMapper.selectSysCpuList(queryParam);
        AtomicInteger index = new AtomicInteger(0);
        List<Integer> indexes = thinningNumbers(cpuList.size(), MAX_ITEM);

        return cpuList.stream().map(v -> {
            Map<String, Object> item = new HashMap<>();
            item.put("value", v.getPercent());
            item.put("time", v.getTime());
            return item;
        }).filter(v -> indexes.indexOf(index.getAndIncrement()) >= 0).collect(Collectors.toList());
    }

    private Map<String, Object> getPersistentCpuInfo(Date firstMinutePeriodTime) {
        SysCpu sysCpu = sysCpuMapper.selectRecentSysCpuWithinTime(firstMinutePeriodTime);
        Map<String, Object> cpuInfo = new LinkedHashMap<>();

        if (sysCpu != null) {
            cpuInfo.put("value", sysCpu.getPercent());
            cpuInfo.put("time", sysCpu.getTime());
        }

        return cpuInfo;
    }

    private List<Map<String, Object>> getPersistentMemoryInfo(Map<String, Object> timeParam) {
        SysMemory queryParam = new SysMemory();
        queryParam.setParams(timeParam);

        List<SysMemory> memoryList = sysMemoryMapper.selectSysMemoryList(queryParam);
        AtomicInteger index = new AtomicInteger(0);
        List<Integer> indexes = thinningNumbers(memoryList.size(), MAX_ITEM);

        return memoryList.stream().map(v -> {
            Map<String, Object> item = new HashMap<>();
            item.put("value", v.getPercent());
            item.put("time", v.getTime());
            return item;
        }).filter(v -> indexes.indexOf(index.getAndIncrement()) >= 0).collect(Collectors.toList());
    }

    private Map<String, Object> getPersistentMemoryInfo(Date firstMinutePeriodTime) {
        SysMemory sysMemory = sysMemoryMapper.selectRecentSysMemoryWithinTime(firstMinutePeriodTime);
        Map<String, Object> memoryInfo = new LinkedHashMap<>();

        if (sysMemory != null) {
            memoryInfo.put("value", sysMemory.getPercent());
            memoryInfo.put("time", sysMemory.getTime());
        }

        return memoryInfo;
    }

    private List<Map<String, Object>> getPersistentDiskInfo(Integer readwrite, Map<String, Object> timeParam) {
        SysDisk queryParam = new SysDisk();
        queryParam.setReadwrite(readwrite);
        queryParam.setParams(timeParam);

        List<SysDisk> diskList = sysDiskMapper.selectSysDiskList(queryParam);
        List<Map<String, Object>> diskInfo = new LinkedList<>();
        int totalCount = diskList.size();
        List<Integer> indexes = thinningNumbers(totalCount, MAX_ITEM);

        for (int i = 1; i < indexes.size(); i++) {
            Map<String, Object> item = new HashMap<>();
            int diskIndex = indexes.get(i);
            SysDisk disk = diskList.get(diskIndex);
            SysDisk preDisk = diskList.get(diskIndex - 1);

            item.put("time", disk.getTime());
            long diffSecond = (disk.getTime().getTime() - preDisk.getTime().getTime()) / 1000;
            double value = (disk.getSpeed() - preDisk.getSpeed()) / diffSecond;
            item.put("value", value < 0 ? 0 : value);

            diskInfo.add(item);
        }

        return diskInfo;
    }

    private Map<String, Object> getPersistentDiskInfo(Integer readwrite, Date firstMinutePeriodTime, Date secondMinutePeriodTime) {
        SysDisk firstMinutePeriodDisk = sysDiskMapper.selectRecentSysDiskWithinTime(readwrite, firstMinutePeriodTime);
        Map<String, Object> diskInfo = new LinkedHashMap<>();

        if (firstMinutePeriodDisk != null) {
            SysDisk secondMinutePeriodDisk = sysDiskMapper.selectRecentSysDiskBetweenTime(readwrite, firstMinutePeriodTime, secondMinutePeriodTime);

            diskInfo.put("time", firstMinutePeriodDisk.getTime());
            if (secondMinutePeriodDisk != null) {
                double value = (firstMinutePeriodDisk.getSpeed() - secondMinutePeriodDisk.getSpeed()) / 60;
                diskInfo.put("value", value < 0 ? 0 : value);
            } else {
                diskInfo.put("value", 0);
            }
        }

        return diskInfo;
    }

    private List<Map<String, Object>> getPersistentNetworkInfo(Integer updown, Map<String, Object> timeParam) {
        SysNetwork queryParam = new SysNetwork();
        queryParam.setUpdown(updown);
        queryParam.setParams(timeParam);

        List<SysNetwork> networkList = sysNetworkMapper.selectSysNetworkList(queryParam);
        List<Map<String, Object>> networkInfo = new LinkedList<>();
        int totalCount = networkList.size();
        List<Integer> indexes = thinningNumbers(totalCount, MAX_ITEM);

        for (int i = 1; i < indexes.size(); i++) {
            Map<String, Object> item = new HashMap<>();
            int networkIndex = indexes.get(i);
            SysNetwork network = networkList.get(networkIndex);
            SysNetwork preNetwork = networkList.get(networkIndex - 1);

            item.put("time", network.getTime());
            long diffSecond = (network.getTime().getTime() - preNetwork.getTime().getTime()) / 1000;
            double value = (network.getSpeed() - preNetwork.getSpeed()) / diffSecond;
            item.put("value", value < 0 ? 0 : value);

            networkInfo.add(item);
        }

        return networkInfo;
    }

    private Map<String, Object> getPersistentNetworkInfo(Integer updown, Date firstMinutePeriodTime, Date secondMinutePeriodTime) {
        SysNetwork firstMinutePeriodNetwork = sysNetworkMapper.selectRecentSysNetworkWithinTime(updown, firstMinutePeriodTime);
        Map<String, Object> networkInfo = new LinkedHashMap<>();

        if (firstMinutePeriodNetwork != null) {
            SysNetwork secondMinutePeriodNetwork = sysNetworkMapper.selectRecentSysNetworkBetweenTime(updown, firstMinutePeriodTime, secondMinutePeriodTime);

            networkInfo.put("time", firstMinutePeriodNetwork.getTime());

            if (secondMinutePeriodNetwork != null) {
                double value = (firstMinutePeriodNetwork.getSpeed() - secondMinutePeriodNetwork.getSpeed()) / 60.0;
                networkInfo.put("value", value < 0 ? 0 : value);
            } else {
                networkInfo.put("value", 0);
            }
        }

        return networkInfo;
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

        long[] ticks = processor.getSystemCpuLoadTicks();

        if (StringUtils.isNull(prevTicks)) {
            cpuInfo.put("sys", 0.00);
            cpuInfo.put("user", 0.00);
            cpuInfo.put("total", 0.00);
            cpuInfo.put("wait", 0.00);
            cpuInfo.put("free", 100.00);

            prevTicks = ticks;
            return cpuInfo;
        }

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

        prevTicks = ticks;
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
        jvmInfo.put("runTime", DateUtils.timeDistance(DateUtils.getNowDate(), DateUtils.getServerStartDate()));
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

    private long[] getDiskReadwrite(HardwareAbstractionLayer hal) {
        List<HWDiskStore> stores = hal.getDiskStores();
        long diskRead = 0;
        long diskWrite = 0;
        for (HWDiskStore store : stores) {
            diskRead += store.getReadBytes();
            diskWrite += store.getWriteBytes();
        }
        return new long[]{diskRead, diskWrite};
    }

    private long[] getNetworkUpdown(HardwareAbstractionLayer hal) {
        List<NetworkIF> networks = hal.getNetworkIFs();
        long netUpByte = 0;
        long netDownByte = 0;
        for (NetworkIF network : networks) {
            netUpByte += network.getBytesSent();
            netDownByte += network.getBytesRecv();
        }
        return new long[]{netUpByte, netDownByte};
    }

    /**
     * 稀疏数（从totalCount里面选取maxCount）
     *
     * @return
     */
    private List<Integer> thinningNumbers(int totalCount, int maxCount) {
        List<Integer> info = new LinkedList<>();
        float step = 1.0f;
        float cursor = totalCount - 1.0f;

        if (totalCount > maxCount) {
            step = (totalCount - 1) * 1.0f / (maxCount - 1);
        }

        for (int index = totalCount - 1; index >= 0; ) {
            if (info.size() == maxCount) {
                break;
            }

            info.add(0, index);
            cursor -= step;
            index = Math.round(cursor);
        }

        return info;
    }
}
