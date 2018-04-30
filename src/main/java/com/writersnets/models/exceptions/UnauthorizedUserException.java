package com.writersnets.models.exceptions;

/**
 * Created by mhenr on 01.10.2017.
 */
public class UnauthorizedUserException extends RuntimeException {
    public UnauthorizedUserException() {
        super();
    }

    public UnauthorizedUserException(String message) {
        super(message);
    }
}
