package org.booklink.models;

/**
 * Created by mhenr on 02.10.2017.
 */
public class Response<T> {
    private int code;
    private T message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }
}
