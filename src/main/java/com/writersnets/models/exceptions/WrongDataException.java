package com.writersnets.models.exceptions;

/**
 * Created by mhenr on 29.01.2018.
 */
public class WrongDataException extends RuntimeException {
    public WrongDataException() {
        super();
    }

    public WrongDataException(String message) {
        super(message);
    }
}
