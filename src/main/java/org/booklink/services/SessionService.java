package org.booklink.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.booklink.models.entities.Session;
import org.booklink.models.entities.User;
import org.booklink.repositories.AuthorRepository;
import org.booklink.repositories.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by mhenr on 20.12.2017.
 */
@Service
@Transactional(readOnly = true)
public class SessionService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private SessionRepository sessionRepository;
    private AuthorRepository authorRepository;
    private Environment environment;

    @Autowired
    public SessionService(final SessionRepository sessionRepository, final AuthorRepository authorRepository, final Environment environment) {
        this.sessionRepository = sessionRepository;
        this.authorRepository = authorRepository;
        this.environment = environment;
    }

    @Transactional
    public void updateSession(final String token) {
        final String signingKey = environment.getProperty("jwt.signing-key");
        final Claims claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody();
        final String username = (String)claims.get("sub");
        final LocalDateTime expireDate = LocalDateTime.ofInstant(claims.getExpiration().toInstant(), ZoneId.systemDefault());

        final Session existedSession = sessionRepository.findByUsername(username);
        if (existedSession != null) {
            existedSession.setExpired(expireDate);
            sessionRepository.save(existedSession);
            final User author = authorRepository.findOne(username);
            if (author != null) {
                author.setOnline(true);
            }
        } else {
            final Session session = new Session();
            final User author = authorRepository.findOne(username);
            if (author != null) {
                session.setAuthor(author);
                session.setExpired(expireDate);
                sessionRepository.save(session);
                author.setOnline(true);
            }
        }
    }

    public long getOnlineUsers() {
        return sessionRepository.count();
    }

    public boolean isUserOnline(final String username) {
        final Session session = sessionRepository.findByUsername(username);
        if (session != null) {
            return true;
        }
        return false;
    }

    @Transactional
    @Scheduled(fixedDelay = 900000) //15 min
    public void removeOldSessions() {
        //logger.info("try to remove old sessions ;)");
        authorRepository.setOfflineStatus(LocalDateTime.now());
        sessionRepository.deleteOldSessions(LocalDateTime.now());
    }
}
