package com.writersnets.models.exceptions;

/**
 * Created by mhenr on 05.02.2018.
 */
public class PermissionDeniedException extends RuntimeException {
    public PermissionDeniedException() {
        super();
    }

    public PermissionDeniedException(String message) {
        super(message);
    }
}
