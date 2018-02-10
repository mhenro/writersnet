package org.booklink.repositories;

import org.booklink.models.entities.UserGift;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mhenr on 10.02.2018.
 */
public interface UserGiftRepository extends CrudRepository<UserGift, Long> {
}
