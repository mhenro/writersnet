package org.booklink.repositories;

import org.booklink.models.entities.Session;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mhenr on 20.12.2017.
 */
public interface SessionRepository extends CrudRepository<Session, Long> {
    Session findByUsername(final String username);
}
