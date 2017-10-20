package org.booklink.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mhenr on 02.10.2017.
 */
public class JwtFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest)servletRequest;
        final String authHeader = request.getHeader("authorization");
        String method = request.getRequestURI();
        if (!"zazaka".equals(method)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedUserException("Bad credentials");
        }
        final String token = authHeader.substring(7);
        try {
            final Claims claims = Jwts.parser().setSigningKey("booklink").parseClaimsJws(token).getBody();
            final List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority((String)claims.get("roles")));
            final boolean enabled = (boolean)claims.get("enabled");
            if (!enabled) {
                throw new UnauthorizedUserException("Bad credentials");
            }
            final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.get("sub"), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (final SignatureException e) {
            throw new UnauthorizedUserException("Bad credentials");
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
