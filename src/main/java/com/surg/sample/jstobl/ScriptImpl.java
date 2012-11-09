package com.surg.sample.jstobl;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.ScriptableObject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.NoSuchElementException;

public class ScriptImpl implements Script {
    private ScriptableObject scope;
    
    public ScriptImpl(String source, String sourceName) {

        Context context = Context.enter();

        try {
            this.scope = new ImporterTopLevel(context);
            org.mozilla.javascript.Script customScript =
                context.compileString(source, sourceName, 1, null);
            customScript.exec(context, scope);
        } finally {
            Context.exit();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createCallProxy(Class<T> interf) {
        return (T) Proxy.newProxyInstance(interf.getClassLoader(), new Class[]{interf}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object... args) throws Throwable {
                return call(method.getReturnType(), method.getName(), args);
            }
        });
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T call(Class<T> resultClass, String name, Object... args) {
        Context context = Context.enter();
        try {
            for (int i = 0; i < args.length; i++)
                args[i] = Context.javaToJS(args[i], scope);
            
            Object funcobj = scope.get(name, scope);
            if(funcobj == null || !(funcobj instanceof Function))
                throw new NoSuchElementException("Function with name \"" + name + "\" does not exist");
            
            Function function = (Function)funcobj;
            Object retval = function.call(context, scope, scope, args);
            
            if(resultClass != null && !resultClass.equals(void.class))
                retval = Context.jsToJava(retval, resultClass);
            
            return (T)retval;
        } finally {
            Context.exit();
        }
    }
}
