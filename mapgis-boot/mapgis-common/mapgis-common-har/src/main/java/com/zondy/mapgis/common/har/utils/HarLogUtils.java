package com.zondy.mapgis.common.har.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import com.zondy.mapgis.common.core.constant.DateContants;
import com.zondy.mapgis.common.core.utils.EnvUtils;
import com.zondy.mapgis.common.core.utils.StringUtils;
import com.zondy.mapgis.common.har.creator.DefaultHarStreamWriter;
import com.zondy.mapgis.common.har.model.*;
import com.zondy.mapgis.system.api.domain.SysHttpAccess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * HAR格式日志工具类
 *
 * @author mapgis
 * @since 2022/11/28 15:44
 */
@Slf4j
public class HarLogUtils {
    public static void exportHar(HttpServletResponse response, String name, String comment, String version, List<SysHttpAccess> records) {
        try {
            File harFile = Paths.get(System.getProperty("java.io.tmpdir"), "harLogs" + UUID.randomUUID().toString() + ".har").toFile();
            // 转换为HarEntry
            DefaultHarStreamWriter harStreamWriter = new DefaultHarStreamWriter.Builder().withCreator(new HarCreator(name, comment, version)).withOutputFile(harFile).build();
            List<HarEntry> entries = HarLogUtils.convertToHarEntries(records);
            for (HarEntry harEntry : entries) {
                harStreamWriter.addEntry(harEntry);
            }
            harStreamWriter.closeHar();
            // 导出最新的har文件
            ServletOutputStream outputStream = null;
            try (FileInputStream inputStream = new FileInputStream(harFile)) {
                // 清空response
                response.reset();
                // 设置response的Header
                response.addHeader("Content-Disposition", "attachment;filename=accesslog.har");
                response.addHeader("Content-Length", "" + harFile.length());
                response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                outputStream = response.getOutputStream();
                IoUtil.copy(inputStream, outputStream);
            } finally {
                IoUtil.close(outputStream);
            }
        } catch (
                IOException e) {
            log.error("导出HarZip出现异常", e);
        }
    }

    public static List<HarEntry> convertToHarEntries(List<SysHttpAccess> records) {
        if (records != null) {
            return records.stream().map(record -> {
                String created = DateUtil.format(record.getAccessTime(), DatePattern.NORM_DATETIME_MS_FORMAT);

                List<HarCookie> harCookies = new ArrayList<>();
                List<HarHeader> harHeaders = new ArrayList<>();
                List<HarQueryString> harQueryStrings = new ArrayList<>();
                SysHttpAccess.MultiPair[] headers = record.getRequestHeaderPairs();
                parseHeaders(headers, harCookies, harHeaders);

                String qs = record.getQueryString();
                if (StringUtils.hasText(qs)) {
                    String[] qsArr = qs.split("&");
                    for (String s : qsArr) {
                        String[] kv = s.split("=");
                        if (2 == kv.length) {
                            HarQueryString param = new HarQueryString(kv[0], kv[1], null);
                            harQueryStrings.add(param);
                        }
                    }
                }
                String requestBody = record.getRequestBody();
                HarPostData postData = null;
                if (StringUtils.hasText(requestBody)) {
                    List<HarParam> params = new ArrayList<>();
                    String[] qsArr = requestBody.split("&");
                    for (String s : qsArr) {
                        String[] kv = s.split("=");
                        if (2 == kv.length) {
                            HarParam param = new HarParam(kv[0], kv[1], "", "", null);
                            params.add(param);
                        }
                    }
                    postData = new HarPostData("", params, "", "");
                }

                HarRequest request = new HarRequest(0L, record.getMethod(), record.getUrl(), "", harCookies, harHeaders, harQueryStrings, postData, 0L, "");

                List<HarHeader> resHarHeaders = new ArrayList<>();
                List<HarCookie> resHarCookies = new ArrayList<>();
                SysHttpAccess.MultiPair[] resHeaders = record.getResponseHeadersPairs();
                parseHeaders(resHeaders, resHarCookies, resHarHeaders);

                HarContent content = new HarContent(record.getResponseBodySize(), 0L, "", "", "");
                HarResponse response = new HarResponse(Math.toIntExact(record.getResponseStatus()), "", "", resHarCookies, resHarHeaders, content, "", 0L, 0L, "");
                return new HarEntry("", created, record.getTime(), request, response, null, null, record.getIpaddr(), "", "");
            }).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 获取logs文件夹下最新的Har文件
     *
     * @return
     */
    public static File getNewestHarLogFile() {
        try {
            Path logsDir = Paths.get(EnvUtils.getCurrentProjectPath(), "logs");
            // 找到最新的文件
            try (Stream<Path> stream = Files.find(logsDir, 1, (p, attr) -> {
                return p.toString().endsWith(".harlog");
            })) {
                Optional<Path> newestPath = stream.max(Comparator.comparing(o -> FileNameUtil.mainName(o.toString())));
                if (newestPath.isPresent()) {
                    return newestPath.get().toFile();
                }
            }

        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 根据当前时间生成最新的har文件名
     *
     * @return
     */
    public static File getNewHarFile() {
        String dateTimeString = DateContants.DATE_TIME_NO_SPLIT_FORMATTER.format(LocalDateTime.now());
        return Paths.get(EnvUtils.getCurrentProjectPath(), "logs", dateTimeString + ".harlog").toFile();
    }

    private static void parseHeaders(SysHttpAccess.MultiPair[] headers, List<HarCookie> harCookies, List<HarHeader> harHeaders) {
        if (headers != null) {
            Optional<SysHttpAccess.MultiPair> optCookie = Arrays.stream(headers).filter(h -> "cookie".equalsIgnoreCase(h.getK())).findFirst();
            if (optCookie.isPresent()) {
                String[] v = optCookie.get().getV();
                if (v != null && v.length > 0) {
                    List<HarCookie> list = Arrays.stream(v[0].split(";")).map(s -> {
                        String[] cookieKv = s.split("=");
                        if (cookieKv.length > 1) {
                            return new HarCookie(cookieKv[0], cookieKv[1], "", "", "", false, false, "");
                        }
                        return null;
                    }).filter(Objects::nonNull).collect(Collectors.toList());
                    harCookies.addAll(list);
                }
            }

            List<HarHeader> list = Arrays.stream(headers).filter(t -> !"cookie".equalsIgnoreCase(t.getK())).map(t -> {
                return new HarHeader(t.getK(), t.getV() != null && t.getV().length > 0 ? t.getV()[0] : "", "");
            }).collect(Collectors.toList());
            harHeaders.addAll(list);
        }
    }

    public static HarResponse parseResponse(HttpServletResponse response) {
        int status = response.getStatus();
        String statusText = null;
        String httpVersion = "HTTP/1.1";
        List<HarCookie> cookies = null;
        List<HarHeader> headers = parseHeaders(response);
        HarContent content = null;
        String redirectURL = null;
        return new HarResponse(status, statusText, httpVersion, cookies, headers, content, redirectURL, 0L, 0L, null);
    }

    public static HarRequest parseRequest(HttpServletRequest request) {
        HarRequest harRequest = null;
        String method = request.getMethod();
        String url = request.getRequestURI();
        String queryString = request.getQueryString();
        if (StringUtils.hasText(queryString)) {
            url = url + "?" + queryString;
        }
        String httpVersion = "HTTP/1.1";
        List<HarCookie> cookies = parseCookies(request.getCookies());
        List<HarHeader> headers = parseHeaders(request);
        List<HarQueryString> harQueryString = parseQueryString(request);
        HarPostData postData = null;
        // 请见org.apache.catalina.connector.Request/parseParameters()
        // multipart/form-data 、application/x-www-form-urlencoded 只允许tomcat自己读，其他读，那么tomcat就无法获取
        boolean shouldRecord = ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)) && "application/x-www-form-urlencoded".equals(request.getContentType());
        if (shouldRecord) {
            postData = parsePostData(request);
        }
        harRequest = new HarRequest(0L, method, url, httpVersion, cookies, headers, harQueryString, postData, -1L, null);
        return harRequest;
    }

    private static HarPostData parsePostData(String contentType, Map<String, String[]> parameterMap) {
        HarPostData postData = null;
        String content = "";

        try {
            // 通过流永远也读不到，tomcat容器只允许ServletInputStream被他自己消费，否则，会导致容器无法获取post内容
            //if (request.getInputStream().available() != 0) {
            //content = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            //}
            // 这里获取解析后的内容
            List<String> paramList = new ArrayList<>();
            parameterMap.forEach((k, v) -> {
                paramList.add(k + "=" + String.join(",", v));
            });
            content = String.join("&", paramList);
            List<HarParam> postDataParams = new ArrayList<>();
            //长度超过1024，截断
            postData = new HarPostData(contentType, postDataParams, content.length() > 1024 ? content.substring(0, 1024) : content, (String) null);
        } catch (Exception e) {
            log.warn(e.getMessage(), e.getCause());
        }
        return postData;
    }

    private static HarPostData parsePostData(HttpServletRequest request) {
        HarPostData postData = null;
        String contentType = request.getContentType();
        String content = "";

        try {
            // 通过流永远也读不到，tomcat容器只允许ServletInputStream被他自己消费，否则，会导致容器无法获取post内容
            //if (request.getInputStream().available() != 0) {
            //content = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            //}
            // 这里获取解析后的内容
            List<String> paramList = new ArrayList<>();
            request.getParameterMap().forEach((k, v) -> {
                paramList.add(k + "=" + String.join(",", v));
            });
            content = String.join("&", paramList);
            List<HarParam> postDataParams = new ArrayList<>();
            // 长度超过1024，截断
            postData = new HarPostData(contentType, postDataParams, content.length() > 1024 ? content.substring(0, 1024) : content, (String) null);
        } catch (Exception e) {
            log.warn(e.getMessage(), e.getCause());
        }
        return postData;
    }

    private static List<HarQueryString> parseQueryString(HttpServletRequest request) {
        List<HarQueryString> qsList = new ArrayList<>();
        String qs = request.getQueryString();
        if (StringUtils.hasText(qs)) {
            String[] qsArr = qs.split("&");
            for (String s : qsArr) {
                String[] kv = s.split("=");
                if (2 == kv.length) {
                    HarQueryString param = new HarQueryString(kv[0], kv[1], null);
                    qsList.add(param);
                }
            }
            return qsList;
        } else {
            return qsList;
        }
    }

    private static List<HarHeader> parseHeaders(HttpServletRequest request) {
        List<HarHeader> headers = new ArrayList<>();
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String header = request.getHeader(name);
            HarHeader harHeader = new HarHeader(name, header, null);
            headers.add(harHeader);
        }
        return headers;
    }

    private static List<HarHeader> parseHeaders(HttpServletResponse response) {
        List<HarHeader> headers = new ArrayList<>();
        Collection<String> names = response.getHeaderNames();
        names.forEach(name -> {
            String value = response.getHeader(name);
            HarHeader harHeader = new HarHeader(name, value, null);
            headers.add(harHeader);
        });
        return headers;
    }

    private static List<HarCookie> parseCookies(Cookie[] cookies) {
        List<HarCookie> harCookies = new ArrayList<>();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                HarCookie harCookie = parseCookie(cookie);
                harCookies.add(harCookie);
            }
        }
        return harCookies;
    }

    private static HarCookie parseCookie(Cookie cookie) {
        return new HarCookie(cookie.getName(), cookie.getValue(), cookie.getPath(), cookie.getDomain(), String.valueOf(cookie.getMaxAge()), cookie.isHttpOnly(), cookie.getSecure(), cookie.getComment());
    }
}
