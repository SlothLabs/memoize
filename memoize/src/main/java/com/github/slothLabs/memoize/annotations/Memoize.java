package com.github.slothLabs.memoize.annotations;

import com.github.slothLabs.memoize.strategy.MemoizationStrategy;
import com.github.slothLabs.memoize.strategy.NullStrategy;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates the annotated method should be memoized - that is,
 * the results should be cached, and future calls with the same
 * parameters should utilize the cached value (if available) instead
 * of executing the method again.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Memoize {
    /**
     * The caching strategy to utilize. Defaults to {@link NullStrategy}.
     *
     * @return the caching strategy to utilize.
     */
    Class<? extends MemoizationStrategy> strategy() default NullStrategy.class;
}
