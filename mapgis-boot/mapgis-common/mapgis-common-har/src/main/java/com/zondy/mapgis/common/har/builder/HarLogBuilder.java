package com.zondy.mapgis.common.har.builder;

import com.zondy.mapgis.common.har.model.*;

import java.util.ArrayList;
import java.util.List;

public class HarLogBuilder {
    private String version = "1.2";
    private HarCreator creator;
    private HarBrowser browser;
    private List<HarPage> pages;
    private List<HarEntry> entries = new ArrayList<>();
    private String comment;

    public HarLogBuilder withVersion(String version) {
        this.version = version;
        return this;
    }

    public HarLogBuilder withCreator(HarCreator creator) {
        this.creator = creator;
        return this;
    }

    public HarLogBuilder withBrowser(HarBrowser browser) {
        this.browser = browser;
        return this;
    }

    public HarLogBuilder withPages(List<HarPage> pages) {
        this.pages = pages;
        return this;
    }

    public HarLogBuilder withEntries(List<HarEntry> entries) {
        this.entries = entries;
        return this;
    }

    public HarLogBuilder addEntry(HarEntry entry) {
        entries.add(entry);
        return this;
    }

    public HarLogBuilder withComment(String comment) {
        this.comment = comment;
        return this;
    }

    public HarLog build() {
        return new HarLog(version, creator, browser, pages, entries, comment);
    }
}