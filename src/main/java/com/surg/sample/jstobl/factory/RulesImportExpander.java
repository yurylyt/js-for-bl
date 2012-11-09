/*******************************************************************************
 * Copyright (c) Aviaso Software Inc.
 * All rights reserved. 
 * No part of this work may be reproduced in any form without the written
 * permission of Aviaso Software Inc.
 * This work is the property of Aviaso Software Inc. and may not be disclosed
 * to any third party without the written permission of Aviaso Software Inc.
 ******************************************************************************/
package com.surg.sample.jstobl.factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RulesImportExpander {
    private static final String IMPORT_STATEMENT_REGEX = "//#import (\\w+)";
    private ScriptReader scriptReader;

    RulesImportExpander(ScriptReader scriptReader) {
        this.scriptReader = scriptReader;
    }

    public String getScript(String key) {
        return expandImports(key, new HashSet<String>());
    }

    private String expandImports(String key, HashSet<String> stack) {
        stack.add(key);
        String scriptBody;
        scriptBody = scriptReader.getScriptBody(key);
        if (scriptBody == null) scriptBody = "";
        
        Pattern importPattern = Pattern.compile(IMPORT_STATEMENT_REGEX);
        StringBuilder result = new StringBuilder(scriptBody.length());
        
        StringReader reader = new StringReader(scriptBody);
        BufferedReader buffered = new BufferedReader(reader);
        try {
            for (String line = buffered.readLine(); line != null; line = buffered.readLine()) {
                Matcher matcher = importPattern.matcher(line);
                if (matcher.matches()) {
                    String importedKey = matcher.group(1);

                    // if stack already contains the key, it means necessary source is already present 
                    // in the resulting script. Therefore, all other imports may be skipped
                    if (!stack.contains(importedKey)) {
                        // Recursive call
                        result.append(expandImports(importedKey, stack));
                    }
                } else {
                    result.append(line);
                    result.append("\n");
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            try {
                buffered.close();
            } catch (IOException e) {
                // Do nothing. It's not possible to use IOUtils.closeQuitely, as this code goes to CPGI
            }
        }
        reader.close();

        return result.toString();
    }
}
