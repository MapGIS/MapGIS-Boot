package com.zondy.mapgis.common.core.logback;

import ch.qos.logback.core.ConsoleAppender;
import com.zondy.mapgis.common.core.utils.EnvironmentUtil;
import org.apache.commons.io.output.NullPrintStream;

import java.io.OutputStream;

/**
 * 自定义的控制台输出appender，解决通过程序启动时，没有控制台输出设备导致线程卡死的问题
 *
 * @author mapgis
 * @since 2022/12/14 10:48
 */
public class CustomConsoleAppender<E> extends ConsoleAppender<E> {
    @Override
    public void setOutputStream(OutputStream outputStream) {
        if (EnvironmentUtil.CONSOLE_OUT_DISABLED) {
            outputStream = NullPrintStream.NULL_PRINT_STREAM;
        }
        super.setOutputStream(outputStream);
    }
}
