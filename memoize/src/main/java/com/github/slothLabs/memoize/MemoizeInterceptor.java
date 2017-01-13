package com.github.slothLabs.memoize;

import com.github.slothLabs.memoize.annotations.Memoize;
import com.github.slothLabs.memoize.strategy.MemoizationStrategy;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.annotation.Nullable;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implements the memoization step.
 */
public final class MemoizeInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(@Nullable final MethodInvocation invocation) throws Throwable {
        checkNotNull(invocation, "Method invocation was null.");

        final Object[] args = invocation.getArguments();
        final Object target = invocation.getThis();
        final Method method = invocation.getMethod();
        final InvocationDetails details = new InvocationDetails(target, args, method, targetQueue, parametersQueue);

        final Memoize memoizeAnnotation = method.getAnnotation(Memoize.class);
        final MemoizationStrategy strategy = getStrategy(memoizeAnnotation);

        return strategy.getResult(details, invocation::proceed);
    }

    private void cleanup() {
        final Object targetRef = targetQueue.poll();
        if (targetRef != null) {
            final Set<InvocationDetails> detailsSet = targetToInvocationDetails.get(targetRef);
            if (detailsSet != null) {
                detailsSet.forEach(cache::remove);
            }
        }
    }

    private final Map<Object, Set<InvocationDetails>> targetToInvocationDetails = new WeakHashMap<>();
    private final Map<InvocationDetails, Object> cache = new ConcurrentHashMap<>();
    private final ReferenceQueue<Object> targetQueue = new ReferenceQueue<>();
    private final ReferenceQueue<Object> parametersQueue = new ReferenceQueue<>();

}
