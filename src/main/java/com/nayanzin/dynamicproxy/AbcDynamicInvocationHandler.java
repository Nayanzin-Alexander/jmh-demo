package com.nayanzin.dynamicproxy;

import static java.util.Optional.ofNullable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class AbcDynamicInvocationHandler implements InvocationHandler {

    private final Map<String, Method> methods;

    private final Object target;

    public AbcDynamicInvocationHandler(Object target) {
        this.target = target;
        this.methods = getMethods();
    }

    private Map<String, Method> getMethods() {
        return Arrays.stream(target.getClass().getDeclaredMethods())
            .collect(Collectors.toMap(Method::getName, m -> m));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        args[0] = Math.abs((int) args[0]);
        return dispatchMethod(method).invoke(target, args);
    }

    private Method dispatchMethod(Method method) {
        return ofNullable(methods.get(method.getName()))
            .orElseThrow(NoSuchElementException::new);
    }
}
