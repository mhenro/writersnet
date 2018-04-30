package com.writersnets.models.exceptions;

/**
 * Created by mhenr on 17.01.2018.
 */
public class IsNotPremiumUserException extends RuntimeException {
    public IsNotPremiumUserException() {
        super();
    }

    public IsNotPremiumUserException(String message) {
        super(message);
    }
}
