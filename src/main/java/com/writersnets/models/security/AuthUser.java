package com.writersnets.models.security;

import com.writersnets.models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class AuthUser implements UserDetails {
    private User user;
    private User userDetails;

    @Autowired
    public AuthUser(User user) {
        this.user = user;
        this.userDetails = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user.getAuthority() == null || user.getAuthority().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(user.getAuthority().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }

    public User getUserDetails() {
        return userDetails;
    }
}
