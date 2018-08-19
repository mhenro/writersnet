package com.writersnets.repositories;

import com.writersnets.models.entities.groups.Message;
import com.writersnets.models.response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by mhenr on 07.12.2017.
 */
public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {
    @Query("SELECT new com.writersnets.models.response.MessageResponse(m.id, m.creator.username, m.creator.firstName, m.creator.lastName, m.creator.avatar, m.message, m.created, m.group.id, m.unread) FROM Message m WHERE m.group.id = ?1 ORDER BY m.created DESC")
    Page<MessageResponse> getMessagesByGroup(final long groupId, final Pageable pageable);

    @Query("SELECT DISTINCT COUNT(m) FROM Message m, User u LEFT JOIN u.chatGroups g WHERE m.creator.username != ?1 AND m.unread = true AND m.group = g AND u.username = ?1")
    Long getUnreadMessages(final String userId);

    @Modifying
    @Query("UPDATE Message m SET m.unread = false WHERE m.group.id = ?2 AND m.creator.username != ?1")
    void markAsReadInGroup(final String userId, final Long groupId);
}
