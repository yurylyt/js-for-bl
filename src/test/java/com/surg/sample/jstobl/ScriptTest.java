package com.surg.sample.jstobl;

import org.testng.annotations.Test;

import java.util.NoSuchElementException;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

public class ScriptTest {
    @Test
    public void testHello() throws Exception {
        // Given
        String expected = "Hello from Rhino";
        String source = "function hello() {return '" + expected + "'}";
        Script script = new ScriptImpl(source, "hello-script");
        
        // When
        String hello = script.call(String.class, "hello");

        // Then
        assertThat(hello).isEqualTo(expected);
    }
    
    @Test
    public void testHello_withParam() throws Exception {
        // Given
        String source = "function hello(name) {return 'Hello, ' + name}";
        Script script = new ScriptImpl(source, "hello-script");
        
        // When
        String hello = script.call(String.class, "hello", "surg");

        // Then
        assertThat(hello).isEqualTo("Hello, surg");
    }
    
    @Test(expectedExceptions = NoSuchElementException.class)
    public void testHello_noFunction() throws Exception {
        // Given
        String source = "function hello(name) {return 'Hello, ' + name}";
        Script script = new ScriptImpl(source, "hello-script");
        
        // When
        String hello = script.call(String.class, "bye", "surg");

        // Then
        fail();
    }
}
