package com.theunitedtraders.javatest.util;

/**
 * This class contains identification-related convenience methods.
 */
public final class IdentificationUtils {

    private IdentificationUtils() {
    }

    /**
     * Returns unique identifying hashcode of current thread.
     * @return integer hashcode value;
     */
    public static int threadId() {
        return System.identityHashCode(Thread.currentThread());
    }
}
