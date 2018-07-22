package com.writersnets.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by mhenr on 02.10.2017.
 */
@Getter @Setter
public class Response<T> {
    private int code;
    private T message;

    public static <T> ResponseEntity<Response<T>> createResponseEntity(final int code, final T message, final String token, final HttpStatus httpStatus) {
        Response<T> response = new Response<>();
        response.setCode(code);
        response.setMessage(message);
        return new ResponseEntity<>(response, httpStatus);
    }
}
