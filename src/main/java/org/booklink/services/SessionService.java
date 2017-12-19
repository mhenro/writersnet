package org.booklink.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.booklink.models.entities.Session;
import org.booklink.repositories.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by mhenr on 20.12.2017.
 */
@Service
public class SessionService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private SessionRepository sessionRepository;

    public SessionService(final SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void updateSession(final String token) {
        final Claims claims = Jwts.parser().setSigningKey("booklink").parseClaimsJws(token).getBody();
        final String username = (String)claims.get("sub");
        final Date expireDate = claims.getExpiration();

        final Session existedSession = sessionRepository.findByUsername(username);
        if (existedSession != null) {
            existedSession.setExpired(expireDate);
            sessionRepository.save(existedSession);
        } else {
            final Session session = new Session();
            session.setUsername(username);
            session.setExpired(expireDate);
            sessionRepository.save(session);
        }
    }

    @Scheduled(fixedDelay = 900000) //15 min
    public void removeOldSessions() {
        logger.info("try to remove old sessions ;)");
    }
}
