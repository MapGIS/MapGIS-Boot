package com.zondy.mapgis.common.core.logback;

import ch.qos.logback.core.ConsoleAppender;
import com.zondy.mapgis.common.core.utils.EnvUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * 自定义的控制台输出appender，解决通过程序启动时，没有控制台输出设备导致线程卡死的问题
 *
 * @author mapgis
 * @since 2022/12/14 10:48
 */
public class CustomConsoleAppender<E> extends ConsoleAppender<E> {
    @Override
    public void setOutputStream(OutputStream outputStream) {
        if (EnvUtils.CONSOLE_OUT_DISABLED) {
            outputStream = NullPrintStream.NULL_PRINT_STREAM;
        }
        super.setOutputStream(outputStream);
    }

    static class NullPrintStream extends PrintStream {
        public static final NullPrintStream NULL_PRINT_STREAM = new NullPrintStream();

        public NullPrintStream() {
            super(NullOutputStream.NULL_OUTPUT_STREAM);
        }
    }

    static class NullOutputStream extends OutputStream {
        public static final NullOutputStream NULL_OUTPUT_STREAM = new NullOutputStream();


        public NullOutputStream() {
        }

        @Override
        public void write(byte[] b, int off, int len) {
        }

        @Override
        public void write(int b) {
        }

        @Override
        public void write(byte[] b) throws IOException {
        }
    }
}
