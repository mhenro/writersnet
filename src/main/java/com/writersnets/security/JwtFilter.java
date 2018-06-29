package com.writersnets.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import com.writersnets.models.exceptions.UnauthorizedUserException;
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
        final String authHeader = request.getHeader("authorization");
        processAuthHeader(authHeader);

        filterChain.doFilter(servletRequest, servletResponse);

        resetAuthentication();
    }

    private void resetAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    private void processAuthHeader(final String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
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
        }
    }
}
