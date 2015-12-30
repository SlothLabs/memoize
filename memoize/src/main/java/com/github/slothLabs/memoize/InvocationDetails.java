package com.github.slothLabs.memoize;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Details for an invocation call.
 */
public final class InvocationDetails {

    public InvocationDetails(final Class<?> targetClass, final Optional<Object> target, final Optional<Object[]> parameters) {
        this.targetClass = checkNotNull(targetClass, "Target class must be provided.");

        this.target = checkNotNull(target, "Target (Optional instance) must be provided.");
        this.parameters = checkNotNull(parameters, "Parameters (Optional instance) must be provided.");

        this.hashCode = Objects.hash(target, parameters);
    }

    public Optional<Object> getTarget() {
        return target;
    }

    public Optional<Object[]> getParameters() {
        return parameters;
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (!(o instanceof InvocationDetails)) return false;
        final InvocationDetails that = (InvocationDetails) o;
        return Objects.equals(target, that.target) &&
                Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    private final Class<?> targetClass;
    private final Optional<Object> target;
    private final Optional<Object[]> parameters;

    private final int hashCode;
}
