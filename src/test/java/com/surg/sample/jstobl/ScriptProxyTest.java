package com.surg.sample.jstobl;

import org.mozilla.javascript.JavaScriptException;
import org.testng.annotations.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ScriptProxyTest {
    @Test
    public void testStatus() throws Exception {
        // Given
        String source = "function getStatusString(status) {if (status == 1) return 'NEW'; else throw 'no such status';}";
        Script script = new ScriptImpl(source, "status-script");
        StatusRules statusRules = script.createCallProxy(StatusRules.class);
        // When
        String string = statusRules.getStatusString(1);

        // Then
        assertThat(string).isEqualTo("NEW");
    }
    
    @Test(expectedExceptions = JavaScriptException.class)
    public void testStatus2() throws Exception {
        // Given
        String source = "function getStatusString(status) {if (status == 1) return 'NEW'; else throw 'no such status';}";
        Script script = new ScriptImpl(source, "status-script");
        StatusRules statusRules = script.createCallProxy(StatusRules.class);
        // When
        String string = statusRules.getStatusString(2);

        // Then
        assertThat(string).isEqualTo("NEW");
    }

    public interface StatusRules {
        String getStatusString(int status);
    }
}
