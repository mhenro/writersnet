package org.booklink.services;

import org.springframework.stereotype.Component;

/**
 * Created by mhenr on 28.09.2017.
 */
@Component
public class TestBean {
    public String getMessage() {
        return "TestBean injected!";
    }
}
