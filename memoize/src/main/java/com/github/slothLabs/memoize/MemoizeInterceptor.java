package com.github.slothLabs.memoize;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implements the memoization step.
 */
final class MemoizeInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final Object[] args = invocation.getArguments();
        final Object target = invocation.getThis();
        final Method method = invocation.getMethod();
        final Class<?> targetClass = method.getDeclaringClass();
        final InvocationDetails details = new InvocationDetails(targetClass, Optional.ofNullable(target), Optional.of(args));

        final Object res = cache.putIfAbsent(details, invocation.proceed());
        return res;
    }

    private final Map<InvocationDetails, Object> cache = new ConcurrentHashMap<>();
}
