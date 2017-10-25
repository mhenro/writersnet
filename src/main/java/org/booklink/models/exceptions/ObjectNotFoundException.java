package org.booklink.models.exceptions;

/**
 * Created by mhenr on 25.10.2017.
 */
public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException() {
        super();
    }

    public ObjectNotFoundException(String message) {
        super(message);
    }
}
