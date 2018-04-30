package com.writersnets.models.exceptions;

/**
 * Created by mhenr on 04.02.2018.
 */
public class WrongCaptchaException extends RuntimeException {
    public WrongCaptchaException() {
        super();
    }

    public WrongCaptchaException(String message) {
        super(message);
    }
}
