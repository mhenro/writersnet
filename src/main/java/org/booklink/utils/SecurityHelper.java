package org.booklink.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.booklink.models.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;

/**
 * Created by mhenr on 19.12.2017.
 */
public final class SecurityHelper {
    private static final long EXPIRATION_DATE = 900000; //15 min
    
    private SecurityHelper() {}

    public static String generateActivationToken(final User user, final String key) {
        String result = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", user.getAuthority())
                .claim("enabled", user.getEnabled())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_DATE))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        return result;
    }

    public static String generateActivationToken(final String key) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        final User user = new User();
        user.setUsername(username);
        user.setAuthority("ROLE_USER");
        user.setEnabled(true);

        return generateActivationToken(user, key);
    }
}
