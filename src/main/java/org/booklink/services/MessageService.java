package org.booklink.services;

import org.booklink.models.entities.*;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.repositories.AuthorRepository;
import org.booklink.repositories.ChatGroupRepository;
import org.booklink.repositories.MessageRepository;
import org.booklink.repositories.UserChatGroupRepository;
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
    private ChatGroupRepository chatGroupRepository;
    private UserChatGroupRepository userChatGroupRepository;

    @Autowired
    public MessageService(final MessageRepository messageRepository,
                          final AuthorRepository authorRepository,
                          final ChatGroupRepository chatGroupRepository,
                          final UserChatGroupRepository userChatGroupRepository) {
        this.messageRepository = messageRepository;
        this.authorRepository = authorRepository;
        this.chatGroupRepository = chatGroupRepository;
        this.userChatGroupRepository = userChatGroupRepository;
    }

    public Page<Message> getMessagesByGroup(final String userId, final Long groupId, final Pageable pageable) {
        checkCredentials(userId);
        return messageRepository.getMessagesByGroupIdOrderByCreatedAsc(groupId, pageable);
    }

    public Long addMessageToGroup(final String creator, final String primaryRecipient, final String text, final Long groupId) {
        checkCredentials(creator);
        final User author = authorRepository.findOne(creator);
        if (author == null) {
            throw new ObjectNotFoundException("Author of the message is not found");
        }
        final User recipient = primaryRecipient != null ? authorRepository.findOne(primaryRecipient) : null;
        if (primaryRecipient != null && recipient == null) {
            throw new ObjectNotFoundException("Recipient is not found");
        }
        final ChatGroup group = getChatGroup(author, groupId, recipient);
        final Message message = new Message();
        message.setCreator(author);
        message.setMessage(text);
        message.setCreated(new Date());
        message.setGroup(group);
        messageRepository.save(message);
        return group.getId();
    }

    public ChatGroup getGroupByRecipient(final String recipientId, final String authorId) {
        checkCredentials(authorId);
        final User author = authorRepository.findOne(authorId);
        if (author == null) {
            throw new ObjectNotFoundException("Author of the message is not found");
        }
        final User recipient = authorRepository.findOne(recipientId);
        if (recipient == null) {
            throw new ObjectNotFoundException("Recipient is not found");
        }
        return getGroupByRecipient(recipient, author);
    }

    public String getGroupName(final Long groupId, final String author) {
        final ChatGroup group = chatGroupRepository.findOne(groupId);
        if (group != null) {
            String groupName = group.getName();
            if (groupName != null) {
                return groupName;
            }
            String recipientName = group.getPrimaryRecipient().getFullName();
            if (!group.getPrimaryRecipient().getUsername().equals(author)) {
                return recipientName;
            } else {
                return group.getCreator().getFullName();
            }
        }
        return null;
    }

    private ChatGroup getGroupByRecipient(final User recipient, final User author) {
        ChatGroup result = null;
        if (recipient != null) {
            ChatGroup group = recipient.getChatGroups().stream()
                    .filter(userChatGroup -> userChatGroup.getUserChatGroupPK().getGroup().getPrimaryRecipient().equals(recipient))
                    .map(userChatGroup -> userChatGroup.getUserChatGroupPK().getGroup())
                    .findAny()
                    .orElseGet(() -> null);
            if (group != null) {
                return group;
            }
            group = new ChatGroup();
            group.setCreated(new Date());
            group.setCreator(author);
            group.setPrimaryRecipient(recipient);
            chatGroupRepository.save(group);
            final UserChatGroup userChatGroup = createUserChatGroup(author, group);
            author.getChatGroups().add(userChatGroup);
            userChatGroupRepository.save(userChatGroup);
            final UserChatGroup recipientChatGroup = createUserChatGroup(recipient, group);
            recipient.getChatGroups().add(recipientChatGroup);
            userChatGroupRepository.save(recipientChatGroup);
            result = group;
        }
        return result;
    }

    private ChatGroup getChatGroup(final User author, final Long groupId, final User recipient) {
        if (groupId != null) {
            return author.getChatGroups().stream()
                    .filter(userChatGroup -> userChatGroup.getUserChatGroupPK().getGroup().getId() == groupId)
                    .map(userChatGroup -> userChatGroup.getUserChatGroupPK().getGroup())
                    .findAny()
                    .orElseThrow(() -> new ObjectNotFoundException("Chat group is not found"));
        }
        ChatGroup group = getGroupByRecipient(recipient, author);
        if (group != null) {
            return group;
        }
        throw new RuntimeException("Undefined behavior");
    }

    private UserChatGroup createUserChatGroup(final User user, final ChatGroup group) {
        final UserChatGroup userChatGroup = new UserChatGroup();
        final UserChatGroupPK pk = new UserChatGroupPK();
        userChatGroup.setUserChatGroupPK(pk);
        pk.setUser(user);
        pk.setGroup(group);
        return userChatGroup;
    }

    private void checkCredentials(final String userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        if (!currentUser.equals(userId)) {
            throw new UnauthorizedUserException();
        }
    }
}
