package org.booklink.repositories;

import org.booklink.models.entities.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by mhenr on 07.12.2017.
 */
public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {
    Page<Message> getMessagesByGroupIdOrderByCreatedAsc(final long groupId, final Pageable pageable);
}
