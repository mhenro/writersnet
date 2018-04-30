package com.writersnets.models.exceptions;

/**
 * Created by mhenr on 15.10.2017.
 */
public class ObjectAlreadyExistException extends RuntimeException {
    public ObjectAlreadyExistException() {
        super();
    }

    public ObjectAlreadyExistException(String message) {
        super(message);
    }
}
