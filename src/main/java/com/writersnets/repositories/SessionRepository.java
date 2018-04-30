package com.writersnets.repositories;

import com.writersnets.models.entities.Session;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

/**
 * Created by mhenr on 20.12.2017.
 */
public interface SessionRepository extends CrudRepository<Session, Long> {
    @Query("SELECT s FROM Session s WHERE s.author.username = ?1")
    Session findByUsername(final String username);

    @Modifying
    @Query("DELETE FROM Session s WHERE s.expired < ?1")
    void deleteOldSessions(final LocalDateTime currentDate);
}
