package org.booklink.repositories;

import org.booklink.models.entities.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mhenr on 02.10.2017.
 */
public interface UserRepository extends CrudRepository<User, String> {
    User findUserByActivationToken(final String activationToken);
    User findUserByEmail(final String email);
}
