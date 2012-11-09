package com.surg.sample.jstobl.factory;

import com.surg.sample.jstobl.factory.RulesFactory.Rules;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.fest.assertions.Assertions.assertThat;

public class RulesFactoryTest {
    private RulesFactory factory;

    @BeforeMethod
    public void setUp() throws Exception {
        ScriptReader reader = new ScriptCpReader("/com/surg/sample/jstobl/rules");
        factory = new RulesFactoryImpl(new RulesImportExpander(reader));
    }

    @Test
    public void testFactory() throws Exception {
        // Given
        StatusRules rules = factory.createRules(StatusRules.class);
        // When
        String string = rules.getStatusString(1);

        // Then
        assertThat(string).isEqualTo("NEW");
    }
    
    @Test
    public void testFactory_no_status() throws Exception {
        // Given
        StatusRules rules = factory.createRules(StatusRules.class);
        // When
        String string = rules.getStatusString(100);

        // Then
        assertThat(string).isNull();
    }
    
    @Rules
    public interface StatusRules {
        String getStatusString(int status);
    }
}
