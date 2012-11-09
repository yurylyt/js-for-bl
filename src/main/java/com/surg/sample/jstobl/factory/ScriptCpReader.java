package com.surg.sample.jstobl.factory;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;

public class ScriptCpReader implements ScriptReader {
    private String pathPrefix;

    public ScriptCpReader(String prefix) {
        pathPrefix = prefix;
    }

    @Override
    public String getScriptBody(String key) {
        try {
            InputStream in = this.getClass().getResourceAsStream(pathPrefix + "/" + key + ".js");
            try {
                return IOUtils.toString(in);
            } finally {
                in.close();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
