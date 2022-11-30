package com.zondy.mapgis.common.har.creator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zondy.mapgis.common.har.model.HarLog;
import com.zondy.mapgis.common.har.model.HarLogRoot;

import java.io.File;
import java.io.IOException;

public class DefaultHarWriter implements HarWriter {

    @Override
    public void writeHarLogToFile(HarLog harLog, File logFile) throws IOException {
        try (JsonGenerator jsonGenerator = new JsonFactory().createGenerator(logFile, JsonEncoding.UTF8)) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            jsonGenerator.setCodec(objectMapper);
            jsonGenerator.writeObject(new HarLogRoot(harLog));
        }
    }
}
