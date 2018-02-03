package org.booklink.models.exceptions;

/**
 * Created by mhenr on 03.02.2018.
 */
public class TextConvertingException extends RuntimeException {
    public TextConvertingException() {
        super();
    }

    public TextConvertingException(String message) {
        super(message);
    }
}
