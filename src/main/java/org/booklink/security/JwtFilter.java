package org.booklink.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mhenr on 02.10.2017.
 */
@Component
public class JwtFilter extends GenericFilterBean {
    private Environment environment;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(environment == null){
            ServletContext servletContext = servletRequest.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            environment = webApplicationContext.getBean(Environment.class);
        }
        final HttpServletRequest request = (HttpServletRequest)servletRequest;
        if (!isSecurityRequest(request)) {  //passing non-secure requests as is
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        final String authHeader = request.getHeader("authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedUserException("Bad credentials");
        }
        final String token = authHeader.substring(7);
        try {
            final String key = environment.getProperty("jwt.signing-key");
            final Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            final List<GrantedAuthority> authorities = new ArrayList<>();
            final String role = (String)claims.get("roles");
            if (role == null) {
                throw new UnauthorizedUserException("Bad credentials");
            }
            authorities.add(new SimpleGrantedAuthority(role));
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

    private boolean isSecurityRequest(HttpServletRequest request) {
        final String method = request.getRequestURI();
        final String methodType = request.getMethod();
        if ("/authors".equals(method) && "POST".equalsIgnoreCase(methodType)) {
            return true;
        }
        if ("/authors/password".equals(method) && "POST".equalsIgnoreCase(methodType)) {
            return true;
        }
        if ("/avatar".equals(method) && "POST".equalsIgnoreCase(methodType)) {
            return true;
        }
        if ("/avatar/restore".equals(method) && "GET".equalsIgnoreCase(methodType)) {
            return true;
        }
        if ("/books".equals(method) && "POST".equalsIgnoreCase(methodType)) {
            return true;
        }
        if (method.startsWith("/books/") && "DELETE".equalsIgnoreCase(methodType)) {
            return true;
        }
        if ("/cover".equals(method) && "POST".equalsIgnoreCase(methodType)) {
            return true;
        }
        if (method.startsWith("/cover/restore") && "GET".equalsIgnoreCase(methodType)) {
            return true;
        }
        if ("/text".equals(method) && "POST".equalsIgnoreCase(methodType)) {
            return true;
        }
        if ("/series".equals(method) && "POST".equalsIgnoreCase(methodType)) {
            return true;
        }
        if ("/authors/subscribe".equals(method) && "POST".equalsIgnoreCase(methodType)) {
            return true;
        }
        if ("/authors/unsubscribe".equals(method) && "POST".equalsIgnoreCase(methodType)) {
            return true;
        }
        if (method.startsWith("/friends/") && "GET".equalsIgnoreCase(methodType)) {
            return true;
        }
        if (method.startsWith("/subscribers") && "GET".equalsIgnoreCase(methodType)) {
            return true;
        }
        if (method.startsWith("/subscriptions") && "GET".equalsIgnoreCase(methodType)) {
            return true;
        }
        if (method.startsWith("/friendship") && "GET".equalsIgnoreCase(methodType)) {
            return true;
        }
        if (method.startsWith("/authors/") && method.endsWith("/groups") && "GET".equalsIgnoreCase(methodType)) {
            return true;
        }
        if (method.contains("/messages/") && "GET".equalsIgnoreCase(methodType)) {
            return true;
        }
        if (method.startsWith("/groups/") && "GET".equalsIgnoreCase(methodType)) {
            return true;
        }
        if ("/groups/get".equals(method) && "POST".equalsIgnoreCase(methodType)) {
            return true;
        }
        if ("/groups/messages/read".equals(method) && "POST".equalsIgnoreCase(methodType)) {
            return true;
        }
        if (method.startsWith("/series/") && "DELETE".equalsIgnoreCase(methodType)) {
            return true;
        }
        if ("/messages/add".equals(method) && "POST".equalsIgnoreCase(methodType)) {
            return true;
        }
        if (method.startsWith("/news") && "GET".equalsIgnoreCase(methodType)) {
            return true;
        }
        if ("/review".equals(method) && "POST".equalsIgnoreCase(methodType)) {
            return true;
        }
        return false;
    }
}
