package com.surg.sample.jstobl.factory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface RulesFactory {
    <T> T createRules(Class<T> fromInterface);
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Rules {
        /** 
         * Specify the key of the corresponding script. If left empty, the key will be obtained by translating 
         * type's name into snake case (e.g. DocumentRules will be mapped on script document_rules)
         * @return key of the corresponding script, or empty string. 
         * */
        String value() default "";
    }
}
