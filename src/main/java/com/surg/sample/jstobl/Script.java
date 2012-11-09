package com.surg.sample.jstobl;

public interface Script {
    <T> T createCallProxy(Class<T> interf);

    <T> T call(Class<T> resultClass, String name, Object... args);
}
