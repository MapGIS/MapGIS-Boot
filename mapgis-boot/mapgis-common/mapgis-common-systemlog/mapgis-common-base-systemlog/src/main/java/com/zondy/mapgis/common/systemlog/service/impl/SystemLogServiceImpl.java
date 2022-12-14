package com.zondy.mapgis.common.systemlog.service.impl;

import ch.qos.logback.classic.Level;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ZipUtil;
import com.zondy.mapgis.common.core.utils.EnvironmentUtil;
import com.zondy.mapgis.common.systemlog.model.LogFileReadState;
import com.zondy.mapgis.common.systemlog.model.SysLoginfoEx;
import com.zondy.mapgis.common.systemlog.model.SysLoginfoList;
import com.zondy.mapgis.common.systemlog.service.ISystemLogService;
import io.krakens.grok.api.Grok;
import io.krakens.grok.api.GrokCompiler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 系统日志服务
 *
 * @author xiongbo
 * @since 2022/11/22 18:22
 */
@Slf4j
@Service
public class SystemLogServiceImpl implements ISystemLogService, InitializingBean {
    /**
     * 日志路径
     */
    public String logDir = Paths.get(EnvironmentUtil.getCurrentProjectPath(), "logs").toAbsolutePath().normalize().toString();

    /**
     * grok对象
     */
    private Grok grok;

    @Override
    public void afterPropertiesSet() throws Exception {
        /* Create a new grokCompiler instance */
        GrokCompiler grokCompiler = GrokCompiler.newInstance();
        grokCompiler.registerDefaultPatterns();
        grokCompiler.registerPatternFromClasspath("/grok_patterns/java", StandardCharsets.UTF_8);
        /* Grok pattern to compile, here JAVALOGBACK logs */
        grok = grokCompiler.compile("%{JAVALOGBACK}");
    }

    /**
     * 获取日志
     *
     * @param logId     文件标识
     * @param position  文件索引
     * @param count
     * @param level
     * @param beginTime
     * @param endTime
     * @param keyword
     * @return
     */
    @Override
    public SysLoginfoList getLogs(String logId, String position, int count, String level, String beginTime, String endTime, String keyword) {
        SysLoginfoList sysLoginfoList = new SysLoginfoList();
        sysLoginfoList.setLogs(new ArrayList<>());

        // 不带后缀文件名
        int logFileIndex = 0;
        int lastLine = 0;
        if (StringUtils.hasText(position)) {
            String[] arr = position.split(":");
            logFileIndex = Integer.parseInt(arr[0]);
            lastLine = Integer.parseInt(arr[1]);
        }
        List<String> logFiles = getSortedLogFiles(logId, logFileIndex);

        if (logFiles == null || logFiles.size() == 0) {
            return sysLoginfoList;
        }
        LogFileReadState readState = new LogFileReadState();
        // 依次读取满足条件的文件
        int i = 0;
        List<SysLoginfoEx> logs = new ArrayList<>();
        for (; i < logFiles.size(); i++) {
            if (sysLoginfoList.getLogs().size() >= count) {
                readState.setLine(logs.get(logs.size() - 1).getLine());
                break;
            }
            // 最后清空，垃圾回收
            logs.clear();
            readState.setFilePath(logFiles.get(i));
            // 第一个文件可能设置了上一次读取的开始位置，如果不为0，则为本次读取的结束行
            logs = getFileLogs(Paths.get(logDir, logFiles.get(i)).toFile(), i == 0 ? lastLine : 0);
            int size = logs.size();
            // 逆序遍历
            for (int j = size - 1; j >= 0; j--) {
                if (sysLoginfoList.getLogs().size() >= count) {
                    // 上一条的位置
                    readState.setLine(logs.get(j + 1).getLine());
                    break;
                }
                // LOG FILTERS
                // level必须大于等于用户过滤值
                if (StringUtils.hasText(level) && !Level.toLevel(logs.get(j).getLevel()).isGreaterOrEqual(Level.toLevel(level))) {
                    continue;
                }
                // 满足时间区间条件
                if (StringUtils.hasText(logs.get(j).getCreateTime())) {
                    if (StringUtils.hasText(beginTime)) {
                        long startSecond = DateUtil.parse(beginTime).getTime();
                        long logSecond = DateUtil.parse(logs.get(j).getCreateTime(), DatePattern.NORM_DATETIME_MS_PATTERN).getTime();
                        if (logSecond < startSecond) {
                            continue;
                        }
                    }
                    if (StringUtils.hasText(endTime)) {
                        long endSecond = DateUtil.parse(endTime).getTime();
                        long logSecond = DateUtil.parse(logs.get(j).getCreateTime(), DatePattern.NORM_DATETIME_MS_PATTERN).getTime();
                        if (logSecond > endSecond) {
                            continue;
                        }
                    }
                }
                // 关键字
                if (StringUtils.hasText(keyword) && !logs.get(j).getMessage().contains(keyword)) {
                    continue;
                }
                sysLoginfoList.getLogs().add(logs.get(j));
            }
        }
        // i跳出for会+1
        boolean isLastFile = i == logFiles.size();
        // 只有当遍历最后一个文件，获取内容从文件开始处，即全部结束
        sysLoginfoList.setHasMore(!(isLastFile && readState.getLine() == 0));
        sysLoginfoList.setPosition(getLogFileIndex(readState.getFilePath()) + ":" + readState.getLine());

        return sysLoginfoList;
    }

    /**
     * 导出日志
     *
     * @param response 响应对象
     * @param logId    文件标识
     */
    @Override
    public void exportLogFilesToZip(HttpServletResponse response, String logId) {
        try {
            String fileNameWithoutExt = logId + "-file";
            File zipFile = Paths.get(System.getProperty("java.io.tmpdir"), "logs" + UUID.randomUUID().toString() + ".zip").toFile();
            File[] logFiles = Paths.get(logDir).toFile().listFiles((dir, name) -> name.startsWith(fileNameWithoutExt));
            if (logFiles != null) {
                ZipUtil.zip(zipFile, false, logFiles);
                ServletOutputStream outputStream = null;
                try (FileInputStream inputStream = new FileInputStream(zipFile)) {
                    // 清空response
                    response.reset();
                    // 设置response的Header
                    response.addHeader("Content-Disposition", "attachment;filename=igserver-logs.zip");
                    response.addHeader("Content-Length", "" + zipFile.length());
                    response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                    outputStream = response.getOutputStream();
                    IoUtil.copy(inputStream, outputStream);
                } finally {
                    IoUtil.close(outputStream);
                }
            } else {
                log.info("没有发现任何日志文件");
            }
        } catch (IOException e) {
            log.error("压缩并导出系统日志文件时出现异常", e);
        }
    }

    /**
     * 根据日志文件的名称获取当前日志文件所在类型的索引
     *
     * @param logFileName 日志文件名
     * @return
     */
    private int getLogFileIndex(String logFileName) {
        String name = FilenameUtils.getBaseName(logFileName);
        int index = name.lastIndexOf('.');
        int i = 0;
        if (index > -1) {
            i = Integer.parseInt(name.substring(index + 1));
        }
        return i;
    }

    /**
     * 获取大于指定索引的所有日志文件列表，已排序过
     *
     * @param logId        日志标识
     * @param logFileIndex 日志文件索引
     * @return
     */
    private List<String> getSortedLogFiles(String logId, int logFileIndex) {
        String fileNameWithoutExt = logId + "-file";

        try (Stream<Path> stream = Files.find(Paths.get(logDir), 1, (p, attr) -> {
            if (attr.isRegularFile()) {
                String name = FilenameUtils.getBaseName(p.toString());
                // 这里包含当前日志文件
                return name.startsWith(fileNameWithoutExt) && getLogFileIndex(p.toString()) >= logFileIndex;
            }
            return false;
        })) {
            return stream.map(t -> t.getFileName().toString()).sorted(new Comparator<String>() {
                // 根据logIndex来排序，优先读取index最小，即最近的日志
                @Override
                public int compare(String o1, String o2) {
                    return getLogFileIndex(o1) - getLogFileIndex(o2);
                }
            }).collect(Collectors.toList());
        } catch (IOException e) {
            log.error("获取logs下日志文件列表出现异常{}", e.getMessage());
        }
        return null;
    }

    /**
     * 由于需要将日志按时间逆序输出，逻辑有点绕，如果第一次，必须将文件读完，如果标记了上一次的获取数据开始的位置，就从0到上一次开始位置读，读完后，截取最新的count条
     *
     * @param logFile 日志文件
     * @param endLine 该文件读取的结束行，上一次获取数据开始的行，如果为0，则全部读
     * @return 读取状态
     */
    private List<SysLoginfoEx> getFileLogs(File logFile, int endLine) {
        List<SysLoginfoEx> readLogs = new ArrayList<>();
        try {
            // 必须读取从 0-lastStartLine，如果
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(logFile),
                    StandardCharsets.UTF_8))) {
                String lineTxt = null;
                // 日志内容，可能存在多行，需要拼接
                StringBuilder message = null;
                SysLoginfoEx logInfo = null;
                // 行号从1开始
                int currentLine = 1;
                boolean isEnd = false;
                while (true) {
                    isEnd = (lineTxt = br.readLine()) == null;
                    if (isEnd) {
                        break;
                    }
                    //当前行必须小于上次开始行
                    if (endLine <= 0 || currentLine < endLine) {
                        Map<String, Object> map = grok.capture(lineTxt);
                        if (!map.isEmpty()) {
                            // 如果存在上一条记录，把上一条记录结束
                            if (message != null) {
                                logInfo.setMessage(message.toString());
                                readLogs.add(logInfo);
                                // 重置日志内容对象
                                message = null;
                            }
                            if (message == null) {
                                logInfo = new SysLoginfoEx();
                                logInfo.setLine(currentLine);
                                logInfo.setCreateTime((String) map.get("time"));
                                logInfo.setLevel((String) map.get("level"));
                                logInfo.setLogger((String) map.get("logger"));
                                logInfo.setPid((String) map.get("pid"));
                                logInfo.setThread((String) map.get("thread"));
                                message = new StringBuilder();
                                message.append((String) map.get("message"));
                            }
                        } else {
                            if (message != null) {
                                message.append(System.lineSeparator()).append(lineTxt);
                            }
                        }
                    }
                    currentLine++;
                }
                // 可能最后一行日志只占用了一行
                if (message != null) {
                    logInfo.setMessage(message.toString());
                    readLogs.add(logInfo);
                    // 重置日志内容对象
                    message = null;
                }
            }
        } catch (Exception e) {
            log.error("read file[{}] logs error:{}", logFile.getAbsolutePath(), e);
        }
        return readLogs;
    }
}
