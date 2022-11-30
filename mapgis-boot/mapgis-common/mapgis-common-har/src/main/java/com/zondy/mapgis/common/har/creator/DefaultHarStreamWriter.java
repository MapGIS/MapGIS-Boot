package com.zondy.mapgis.common.har.creator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zondy.mapgis.common.har.model.HarBrowser;
import com.zondy.mapgis.common.har.model.HarCreator;
import com.zondy.mapgis.common.har.model.HarEntry;
import com.zondy.mapgis.common.har.model.HarPage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DefaultHarStreamWriter implements HarStreamWriter {

    private final JsonGenerator jsonGenerator;

    private DefaultHarStreamWriter(File harFile, String version, HarCreator creator, HarBrowser browser, List<HarPage> pages, String comment, boolean usePrettyPrint) throws IOException {
        jsonGenerator = new JsonFactory().createGenerator(harFile, JsonEncoding.UTF8);
        final ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        jsonGenerator.setCodec(objectMapper);
        if (usePrettyPrint) {
            jsonGenerator.useDefaultPrettyPrinter();
        }

        jsonGenerator.writeStartObject();
        writeHarLogFields(version, creator, browser, pages, comment);
        jsonGenerator.writeFieldName("entries");
        jsonGenerator.writeStartArray();
    }

    private void writeHarLogFields(String version, HarCreator creator, HarBrowser browser, List<HarPage> pages, String comment) throws IOException {
        jsonGenerator.writeFieldName("log");
        jsonGenerator.writeStartObject();

        // Add optional fields if they are not null
        if (comment != null) {
            jsonGenerator.writeFieldName("comment");
            jsonGenerator.writeObject(comment);
        }
        if (browser != null) {
            jsonGenerator.writeFieldName("browser");
            jsonGenerator.writeObject(browser);
        }
        if (pages != null) {
            jsonGenerator.writeFieldName("pages");
            jsonGenerator.writeObject(pages);
        }

        jsonGenerator.writeFieldName("creator");
        jsonGenerator.writeObject(creator);
        jsonGenerator.writeFieldName("version");
        jsonGenerator.writeObject(version);
    }

    @Override
    public void addEntry(HarEntry harEntry) throws IOException {
        jsonGenerator.writeObject(harEntry);
        jsonGenerator.flush();
    }

    @Override
    public void closeHar() throws IOException {
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
        jsonGenerator.close();
    }

    public static class Builder {
        private File harFile;
        private String version = "1.2";
        private HarCreator creator;
        private HarBrowser browser;
        private List<HarPage> pages;
        private String comment;
        private boolean usePrettyPrint = false;

        public Builder withVersion(String version) {
            this.version = version;
            return this;
        }

        public Builder withCreator(HarCreator creator) {
            this.creator = creator;
            return this;
        }

        public Builder withBrowser(HarBrowser browser) {
            this.browser = browser;
            return this;
        }

        public Builder withPages(List<HarPage> pages) {
            this.pages = pages;
            return this;
        }

        public Builder addPage(HarPage page) {
            if (pages == null) {
                pages = new ArrayList<>();
            }
            pages.add(page);
            return this;
        }

        public Builder withComment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder withOutputFile(File harFile) {
            this.harFile = harFile;
            return this;
        }

        public Builder withUsePrettyPrint(boolean usePrettyPrint) {
            this.usePrettyPrint = usePrettyPrint;
            return this;
        }


        public DefaultHarStreamWriter build() throws IOException {

            return new DefaultHarStreamWriter(harFile, version, creator, browser, pages, comment, usePrettyPrint);
        }
    }
}