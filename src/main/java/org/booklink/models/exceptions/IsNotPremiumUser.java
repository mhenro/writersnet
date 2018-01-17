package org.booklink.models.exceptions;

/**
 * Created by mhenr on 17.01.2018.
 */
public class IsNotPremiumUser extends RuntimeException {
    public IsNotPremiumUser() {
        super();
    }

    public IsNotPremiumUser(String message) {
        super(message);
    }
}
