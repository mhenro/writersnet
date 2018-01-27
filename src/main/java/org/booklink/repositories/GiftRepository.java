package org.booklink.repositories;

import org.booklink.models.entities.Gift;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mhenr on 27.01.2018.
 */
public interface GiftRepository extends CrudRepository<Gift, Long> {
}
