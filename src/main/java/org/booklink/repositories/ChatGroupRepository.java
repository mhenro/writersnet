package org.booklink.repositories;

import org.booklink.models.entities.ChatGroup;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by mhenr on 10.12.2017.
 */
public interface ChatGroupRepository extends PagingAndSortingRepository<ChatGroup, Long> {
}
