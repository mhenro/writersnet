package org.booklink.services;

import org.booklink.models.entities.Message;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Created by mhenr on 07.12.2017.
 */
@Service
public class MessageService {
    private MessageRepository messageRepository;

    @Autowired
    public MessageService(final MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Page<Message> getMessagesByGroup(final String userId, final Long groupId, final Pageable pageable) {
        checkCredentials(userId);
        return messageRepository.getMessagesByGroupIdOrderByCreatedAsc(groupId, pageable);
    }

    private void checkCredentials(final String userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        if (!currentUser.equals(userId)) {
            throw new UnauthorizedUserException();
        }
    }
}
