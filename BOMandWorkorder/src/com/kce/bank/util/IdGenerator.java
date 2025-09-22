package com.kce.bank.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple ID generator used across the system.
 */
public final class IdGenerator {
    private static final AtomicInteger COUNTER = new AtomicInteger(1000);

    private IdGenerator() {}

    public static String next(String prefix) {
        return prefix + "-" + COUNTER.getAndIncrement();
    }
}
