package org.booklink.repositories;

import org.booklink.models.entities.ChatGroup;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mhenr on 10.12.2017.
 */
public interface ChatGroupRepository extends CrudRepository<ChatGroup, Long> {
}
