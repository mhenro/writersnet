package com.writersnets.models.exceptions;

/**
 * Created by mhenr on 28.01.2018.
 */
public class NotEnoughMoneyException extends RuntimeException{
    public NotEnoughMoneyException() {
        super();
    }

    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
