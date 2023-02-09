package com.zondy.mapgis.common.core.wrapper;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * 属性配置包装类
 *
 * @author Chelsea
 * @since 2023/1/10 18:10
 */
@Slf4j
public class PropertiesWrapper extends Properties {
    private static final long serialVersionUID = 1L;

    private final List<Object> keyList = new ArrayList<>();

    /**
     * 从指定路径加载信息到Properties
     */
    public PropertiesWrapper(String path) {
        try (InputStream is = new BufferedInputStream(new FileInputStream(path))) {
            this.load(new InputStreamReader(is, StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            log.error("文件路径不存在");
        } catch (IOException e) {
            log.error("获取输入错误");
        }
    }

    /**
     * 重写put方法，按照property的存入顺序保存key到keyList，遇到重复的后者将覆盖前者。
     */
    @Override
    public synchronized Object put(Object key, Object value) {
        this.removeKeyIfExists(key);
        keyList.add(key);
        return super.put(key, value);
    }

    /**
     * 重写remove方法，删除属性时清除keyList中对应的key。
     */
    @Override
    public synchronized Object remove(Object key) {
        this.removeKeyIfExists(key);
        return super.remove(key);
    }

    /**
     * 获取Properties中key的有序集合
     */
    public List<Object> getKeyList() {
        return keyList;
    }

    /**
     * 保存Properties到指定文件，默认使用UTF-8编码
     */
    public boolean store(String path) {
        return this.store(path, "UTF-8");
    }

    /**
     * 保存Properties到指定文件，并指定对应存放编码
     */
    public boolean store(String path, String charset) {
        if (path != null && !"".equals(path)) {
            try (OutputStream os = Files.newOutputStream(Paths.get(path))) {
                try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, charset))) {
                    this.store(bw, null);
                }
                return true;
            } catch (IOException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 重写keys方法，返回根据keyList适配的Enumeration，且保持HashTable keys()方法的原有语义，
     * 每次都调用返回一个新的Enumeration对象，且和之前的不产生冲突
     */
    @Override
    public synchronized Enumeration<Object> keys() {
        return new EnumerationAdapter<>(keyList);
    }

    /**
     * keyList中存在指定的key时则将其删除
     */
    private void removeKeyIfExists(Object key) {
        keyList.remove(key);
    }

    private static class EnumerationAdapter<T> implements Enumeration<T> {
        private int index = 0;
        private final List<T> list;
        private final boolean isEmpty;

        public EnumerationAdapter(List<T> list) {
            this.list = list;
            this.isEmpty = list.isEmpty();
        }

        @Override
        public boolean hasMoreElements() {
            // isEmpty的引入是为了更贴近HashTable原有的语义，在HashTable中添加元素前调用其keys()方法获得一个Enumeration的引用，
            // 之后往HashTable中添加数据后，调用之前获取到的Enumeration的hasMoreElements()将返回false，但如果此时重新获取一个
            // Enumeration的引用，则新Enumeration的hasMoreElements()将返回true，而且之后对HashTable数据的增、删、改都是可以在
            // nextElement中获取到的
            return !isEmpty && index < list.size();
        }

        @Override
        public T nextElement() {
            if (this.hasMoreElements()) {
                return list.get(index++);
            }
            return null;
        }
    }
}
