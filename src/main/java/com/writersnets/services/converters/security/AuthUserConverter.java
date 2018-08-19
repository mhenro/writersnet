package com.writersnets.services.converters.security;

import com.writersnets.models.entities.users.User;
import com.writersnets.models.security.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

import java.util.HashMap;
import java.util.Map;

public class AuthUserConverter implements UserAuthenticationConverter {
    private Environment env;

    @Autowired
    public AuthUserConverter(Environment env) {
        this.env = env;
    }

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication userAuthentication) {
        Map<String, Object> tokenContent = new HashMap<>();
        AuthUser principal = (AuthUser)userAuthentication.getPrincipal();
        User user = principal.getUserDetails();
        if (user != null) {
            tokenContent.put("iss", env.getProperty("security.oauth2.client.issuer"));
            tokenContent.put("aud", env.getProperty("security.oauth2.client.client-id"));
            tokenContent.put("sub", user.getUsername());
            tokenContent.put("email", user.getEmail());
            tokenContent.put("roles", user.getAuthority());
            return tokenContent;
        }
        return null;
    }

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        User user = new User();
        user.setUsername((String)map.get("sub"));
        user.setEmail((String)map.get("email"));
        user.setPassword("");
        user.setAuthority((String)map.get("roles"));
        AuthUser principal = new AuthUser(user);
        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    }
}
