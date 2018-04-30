package com.writersnets.models;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by mhenr on 02.10.2017.
 */
public class Response<T> {
    private int code;
    private T message;
    private String token;

    public static <T> ResponseEntity<Response<T>> createResponseEntity(final int code, final T message, final String token, final HttpStatus httpStatus) {
        Response<T> response = new Response<>();
        response.setCode(code);
        response.setMessage(message);
        response.setToken(token);
        return new ResponseEntity<>(response, httpStatus);
    }

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
