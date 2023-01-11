package com.zondy.mapgis.common.core.utils.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * XML 工具类
 *
 * @author xiongbo
 * @since 2023/1/10 15:46
 */
@Slf4j
public class XmlSerialUtils {
    /**
     * 不对特殊字符做处理 Xpp3Driver无法反序列化
     */
    private static final XStream XS = new XStream(new XppDriver(new NoNameCoder()));

    public static XStream getXstream() {
        return XS;
    }

    /**
     * XML序列化
     *
     * @param obj Java对象(不包含List对象)
     * @return 序列化字符串
     */
    public static String toXML(Object obj) {
        String str = null;
        try {
            if (obj != null) {
                XS.processAnnotations(obj.getClass());
                str = XS.toXML(obj);
            }
        } catch (Exception e) {
            log.warn("xml序列化失败", e);
        }
        return str;
    }

    /**
     * XML序列化
     *
     * @param obj  Java对象
     * @param path 待保存xml内容的文件路径
     */
    public static void toXMLAndSaveFile(Object obj, String path) {
        try {
            if (obj != null) {
                XS.processAnnotations(obj.getClass());
                try (FileOutputStream out = new FileOutputStream(path)) {
                    try (Writer w = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {
                        XS.toXML(obj, w);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("xml序列化失败", e);
        }
    }

    /**
     * XML反序列化
     *
     * @param strXml 字符串
     * @param cls    对象类
     * @return Java对象
     */
    public static Object fromXML(String strXml, Class<?> cls) {
        Object obj = null;
        try {
            if (strXml != null && !"".equals(strXml)) {
                XS.setClassLoader(cls.getClassLoader());
                XS.processAnnotations(cls);
                obj = XS.fromXML(strXml);
            }
        } catch (Exception e) {
            log.warn("xml反序列化失败", e);
        }
        return obj;
    }

    /**
     * XML反序列化
     *
     * @param xmlPath 待保存的xml文件路径
     * @param cls     对象类
     * @return List对象
     */
    public static Object fromXMLFile(String xmlPath, Class<?> cls) {
        Object obj = null;
        try {
            File file = new File(xmlPath);
            if (file.exists()) {
                XS.setClassLoader(cls.getClassLoader());
                XS.processAnnotations(cls);
                try (FileInputStream in = new FileInputStream(file)) {
                    try (Reader r = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                        obj = XS.fromXML(r);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("xml反序列化失败", e);
        }
        return obj;
    }
}
