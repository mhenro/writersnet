package org.booklink.repositories;

import org.booklink.models.entities.Session;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by mhenr on 20.12.2017.
 */
public interface SessionRepository extends CrudRepository<Session, Long> {
    @Query("SELECT s FROM Session s WHERE s.author.username = ?1")
    Session findByUsername(final String username);

    @Transactional
    @Modifying
    @Query("DELETE FROM Session s WHERE s.expired < ?1")
    void deleteOldSessions(final Date currentDate);
}
