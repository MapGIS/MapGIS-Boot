package com.zondy.mapgis.common.har.creator;

import com.zondy.mapgis.common.har.model.HarLog;

import java.io.File;
import java.io.IOException;

/**
 * Writes HAR log to an to the given file.
 */
public interface HarWriter {
    /**
     * Writes the HarLog to the provided file.
     *
     * @param harLog  the harLog needs to be written.
     * @param logFile the file where harlLog needs to be written.
     * @throws IOException if the harLog cannot be written
     */
    void writeHarLogToFile(HarLog harLog, File logFile) throws IOException;
}
