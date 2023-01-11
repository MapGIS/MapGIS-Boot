package com.zondy.mapgis.common.service.constant;

import java.util.regex.Pattern;

/**
 * 正则表达式常量
 *
 * @author Chelsea
 * @since 2023/1/10 16:24
 */
public class RegexConstants {
    /**
     * 1个或多个正数，使用‘,’分割
     * 其中?:不缓存group，注意*后面的+时java中防止回溯的方式，js中不支持,使用Jshell测试 https://blog.csdn.net/weixin_33775572/article/details/89009102
     */
    public static final Pattern ONE_OR_MORE_POSITIVE_NUM = Pattern.compile("^[1-9]\\d*(?:,[1-9]\\d*)*+$");

    /**
     * 多个正数，使用‘,’分割
     */
    public static final Pattern MANY_POSITIVE_NUM = Pattern.compile("^[1-9]\\d*(?:,[1-9]\\d*)++$");

    /**
     * 正数
     */
    public static final Pattern POSITIVE_NUM = Pattern.compile("^[1-9]\\d*$");

    /**
     * 非负数
     */
    public static final Pattern NO_NEGATIVE_NUM = Pattern.compile("^(0|[1-9]\\d*)$");

    public static final Pattern DATE_TIME = Pattern.compile("^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$");

    public static final Pattern DATE = Pattern.compile("^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$");

    /**
     * 服务的名称或者服务文件夹，必须为只能包括中文字母数字-_，且不能以-或数字开头
     */
    public static final Pattern SERVICE_NAME = Pattern.compile("^[a-zA-Z\\u4e00-\\u9fa5\\\\_][a-zA-Z0-9\\u4e00-\\u9fa5\\\\_-]+$");
}
