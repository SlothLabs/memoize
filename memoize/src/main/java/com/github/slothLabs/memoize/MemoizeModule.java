package com.github.slothLabs.memoize;

import com.github.slothLabs.memoize.annotations.Memoize;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

/**
 * Created by mcory on 12/29/15.
 */
public final class MemoizeModule extends AbstractModule {
    @Override
    protected void configure() {
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Memoize.class), new MemoizeInterceptor());
    }
}
