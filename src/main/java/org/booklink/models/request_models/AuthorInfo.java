package org.booklink.models.request_models;

/**
 * Created by mhenr on 06.11.2017.
 */
public class AuthorInfo {
    private String avatar;
    private String firstName;
    private String lastName;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
