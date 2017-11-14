package org.booklink.models.request_models;

import com.sun.xml.internal.txw2.annotation.XmlElement;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by mhenr on 30.09.2017.
 */
public class Credentials {
    private String email;
    private String username;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}