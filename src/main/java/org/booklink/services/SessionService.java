package org.booklink.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.booklink.models.entities.Session;
import org.booklink.models.entities.User;
import org.booklink.repositories.AuthorRepository;
import org.booklink.repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by mhenr on 20.12.2017.
 */
@Service
public class SessionService {
    //private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private SessionRepository sessionRepository;
    private AuthorRepository authorRepository;

    @Autowired
    public SessionService(final SessionRepository sessionRepository, final AuthorRepository authorRepository) {
        this.sessionRepository = sessionRepository;
        this.authorRepository = authorRepository;
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
            final User user = authorRepository.findOne(username);
            if (user != null) {
                session.setAuthor(user);
                session.setExpired(expireDate);
                sessionRepository.save(session);
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

    @Scheduled(fixedDelay = 900000) //15 min
    public void removeOldSessions() {
        //logger.info("try to remove old sessions ;)");
        sessionRepository.deleteOldSessions(new Date());
    }
}
