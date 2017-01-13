package com.github.slothLabs.memoize.guice;

import com.github.slothLabs.memoize.MemoizeInterceptor;
import com.github.slothLabs.memoize.annotations.Memoize;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

/**
 * Module for guice-based installation.
 */
public final class MemoizeModule extends AbstractModule {
    @Override
    protected void configure() {
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Memoize.class), new MemoizeInterceptor());
    }
}
