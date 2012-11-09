/*******************************************************************************
 * Copyright (c) Aviaso Software Inc.
 * All rights reserved. 
 * No part of this work may be reproduced in any form without the written
 * permission of Aviaso Software Inc.
 * This work is the property of Aviaso Software Inc. and may not be disclosed
 * to any third party without the written permission of Aviaso Software Inc.
 ******************************************************************************/
package com.surg.sample.jstobl.factory;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;

/**
 * Handles scripts stored in file system. Useful for development purpose.
 * <p/>
 * Author: Iurii Lytvynenko
 */
public class ScriptFsReader implements ScriptReader {
    private File scriptsHome;

    public ScriptFsReader(File scriptsHome) {
        this.scriptsHome = scriptsHome;
    }

    @Override
    public String getScriptBody(String key) {
        try {
            File scriptFile = new File(scriptsHome, key + ".js");
            if (!scriptFile.exists())
                throw new IllegalArgumentException("No script found for key: " + key);
            
            FileInputStream in = new FileInputStream(scriptFile);
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
