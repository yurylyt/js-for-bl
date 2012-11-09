package com.surg.sample.jstobl.factory;

import com.surg.sample.jstobl.ScriptImpl;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RulesFactoryImpl implements RulesFactory {
    private Logger LOGGER = Logger.getLogger(RulesFactoryImpl.class.getName());
    private RulesImportExpander expander;

    public RulesFactoryImpl(RulesImportExpander expander) {
        this.expander = expander;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T createRules(Class<T> fromInterface) {
        String key = getScriptKey(fromInterface);

        LOGGER.log(Level.FINE, "Create rules for key: " + key);
        String body = getScriptBody(key);
        return createScriptCallProxy(key, body, fromInterface);
    }

    private String getScriptBody(String scriptKey) {
        return this.expander.getScript(scriptKey);
    }

    // Public to be accessible from the test.
    public <T> T createScriptCallProxy(String key, String source, Class<T> fromInterface) {
        source = decorateWithToString(source, key);
        return new ScriptImpl(source, key).createCallProxy(fromInterface);
    }

    /**
     * Adds toString() method to the script body. It is useful for debugging and testing purposes.
     *
     * @param source script source
     * @param key    script key. Used in toString() output.
     * @return script source with toString function added
     */
    private String decorateWithToString(String source, String key) {
        String toString = String.format("function toString() { return '%s#%d'}\n", key, System.currentTimeMillis());
        return toString + source;
    }

    String getScriptKey(Class<?> fromInterface) {
        Rules rulesAnnotation = fromInterface.getAnnotation(Rules.class);
        if (rulesAnnotation == null) {
            throw new IllegalArgumentException("The type must be annotated with @Rules");
        }

        String key = rulesAnnotation.value();
        if (key == null || key.length() == 0) {
            key = deriveScriptKey(fromInterface.getSimpleName());
        }
        return key;
    }

    String deriveScriptKey(String simpleName) {
        StringBuilder key = new StringBuilder();
        for (char c : simpleName.toCharArray()) {
            if (Character.isUpperCase(c)) {
                c = Character.toLowerCase(c);
                if (key.length() > 0) { // i.e. insert for all but first letter.
                    key.append("_");
                }
            }
            key.append(c);
        }
        return key.toString();
    }
}
