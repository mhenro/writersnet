package org.booklink.utils;

/**
 * Created by mhenr on 19.03.2018.
 */
public final class ControllerHelper {
    private ControllerHelper() {}

    public static String getErrorOrDefaultMessage(final Exception e, final String defaultMessage) {
        if (e != null && e.getMessage() != null) {
            return e.getMessage();
        }
        return defaultMessage;
    }
}
