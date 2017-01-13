package com.github.slothLabs.memoize;

import javax.annotation.Nullable;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Details for an invocation call.
 */
public final class InvocationDetails {

    public InvocationDetails(final Object target, final Object[] parameters, final Method method, final ReferenceQueue<Object> targetQueue, final ReferenceQueue<Object> parametersQueue) {
        checkNotNull(parameters, "Parameters must be provided.");

        this.target = new WeakReference<>(target, targetQueue);
        this.method = checkNotNull(method, "Method must be provided.");
        final int numParams = parameters.length;
        this.parameters = new WeakObjectReference[numParams];

        for (int i = 0; i < numParams; ++i) {
            this.parameters[i] = new WeakObjectReference(parameters[i], parametersQueue);
        }

        this.hashCode = Objects.hash(this.target, this.parameters, this.method);
    }

    public boolean isValid() {
        boolean res = ((target.get() != null) && (!target.isEnqueued()));

        if (res) {
            for (final WeakObjectReference param : parameters) {
                if (param.get() == null || param.isEnqueued()) {
                    res = false;
                    break;
                }
            }
        }

        return res;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (!(o instanceof InvocationDetails)) return false;
        final InvocationDetails that = (InvocationDetails) o;

        final Object[] thisParams = this.parameters;
        final Object[] thatParams = that.parameters;
        return (this.target == that.target) &&
                Arrays.equals(thisParams, thatParams) &&
                Objects.equals(this.method, that.method);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    private final WeakReference<Object> target;
    private final WeakObjectReference[] parameters;
    private final Method method;


    private final int hashCode;

    private static class WeakObjectReference extends WeakReference<Object> {

        public WeakObjectReference(final Object referent, final ReferenceQueue<? super Object> queue) {
            super(referent, queue);
        }
    }
}
