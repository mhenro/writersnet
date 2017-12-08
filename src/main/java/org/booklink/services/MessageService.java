package org.booklink.services;

import org.booklink.models.entities.ChatGroup;
import org.booklink.models.entities.Message;
import org.booklink.models.entities.User;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.repositories.AuthorRepository;
import org.booklink.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by mhenr on 07.12.2017.
 */
@Service
public class MessageService {
    private MessageRepository messageRepository;
    private AuthorRepository authorRepository;

    @Autowired
    public MessageService(final MessageRepository messageRepository, final AuthorRepository authorRepository) {
        this.messageRepository = messageRepository;
        this.authorRepository = authorRepository;
    }

    public Page<Message> getMessagesByGroup(final String userId, final Long groupId, final Pageable pageable) {
        checkCredentials(userId);
        return messageRepository.getMessagesByGroupIdOrderByCreatedAsc(groupId, pageable);
    }

    public void addMessageToGroup(final String creator, final String text, final long groupId) {
        checkCredentials(creator);
        final User author = authorRepository.findOne(creator);
        if (author == null) {
            throw new ObjectNotFoundException("Author of the message is not found");
        }
        final ChatGroup group = author.getChatGroups().stream()
                .filter(userChatGroup -> userChatGroup.getUserChatGroupPK().getGroup().getId() == groupId)
                .map(userChatGroup -> userChatGroup.getUserChatGroupPK().getGroup())
                .findAny()
                .orElseThrow(() -> new ObjectNotFoundException("Chat group is not found"));
        final Message message = new Message();
        message.setCreator(author);
        message.setMessage(text);
        message.setCreated(new Date());
        message.setGroup(group);
        messageRepository.save(message);
    }

    private void checkCredentials(final String userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        if (!currentUser.equals(userId)) {
            throw new UnauthorizedUserException();
        }
    }
}
