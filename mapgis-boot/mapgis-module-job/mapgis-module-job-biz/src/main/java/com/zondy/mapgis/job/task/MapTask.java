package com.zondy.mapgis.job.task;

import com.zondy.mapgis.common.core.utils.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 定时任务调度测试
 *
 * @author xiongbo
 * @since 2021/12/10 9:53
 */
@Component("mapTask")
public class MapTask {
    public void mapMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void mapParams(String params) {
        System.out.println("执行有参方法：" + params);
    }

    public void mapNoParams() {
        System.out.println("执行无参方法");
    }
}
