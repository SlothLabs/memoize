package com.github.slothLabs.memoize.strategy;

import com.github.slothLabs.memoize.InvocationDetails;

import java.lang.ref.ReferenceQueue;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Indicates usage of naive (i.e. {@link java.util.concurrent.ConcurrentHashMap}
 * strategy with no real management outside of weak reference tracking.
 */
public class DefaultStrategy implements MemoizationStrategy {

    public Object blah() {
        final Set<InvocationDetails> detailsSet;
        if (targetToInvocationDetails.containsKey(target)) {
            detailsSet = targetToInvocationDetails.get(target);
        } else {
            detailsSet = new HashSet<>();
            targetToInvocationDetails.put(target, detailsSet);
        }

        detailsSet.add(details);

        final Object res;
        if (cache.containsKey(details)) {
            res = cache.get(details);
        } else {
            res = invocation.proceed();
            cache.put(details, res);
        }

        cleanup();

        return res;
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
