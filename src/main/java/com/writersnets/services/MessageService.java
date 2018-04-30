package com.writersnets.services;

import com.writersnets.models.entities.*;
import com.writersnets.models.exceptions.ObjectNotFoundException;
import com.writersnets.models.exceptions.UnauthorizedUserException;
import com.writersnets.models.response.MessageResponse;
import com.writersnets.repositories.AuthorRepository;
import com.writersnets.repositories.ChatGroupRepository;
import com.writersnets.repositories.FriendshipRepository;
import com.writersnets.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by mhenr on 07.12.2017.
 */
@Service
@Transactional(readOnly = true)
public class MessageService {
    private MessageRepository messageRepository;
    private AuthorRepository authorRepository;
    private ChatGroupRepository chatGroupRepository;
    private FriendshipRepository friendshipRepository;

    @Autowired
    public MessageService(final MessageRepository messageRepository,
                          final AuthorRepository authorRepository,
                          final ChatGroupRepository chatGroupRepository,
                          final FriendshipRepository friendshipRepository) {
        this.messageRepository = messageRepository;
        this.authorRepository = authorRepository;
        this.chatGroupRepository = chatGroupRepository;
        this.friendshipRepository = friendshipRepository;
    }

    public Page<MessageResponse> getMessagesByGroup(final String userId, final Long groupId, final Pageable pageable) {
        checkCredentials(userId);
        return messageRepository.getMessagesByGroup(groupId, pageable);
    }

    @Transactional
    public Long addMessageToGroup(final String creator, final String primaryRecipient, final String text, final Long groupId) {
        checkCredentials(creator);
        final User author = authorRepository.findById(creator).orElseThrow(() -> new ObjectNotFoundException("Author of the message is not found"));
        final User recipient = primaryRecipient != null ? authorRepository.findById(primaryRecipient).orElseThrow(() -> new ObjectNotFoundException("Recipient is not found")) : null;
        final ChatGroup group = getChatGroup(author, groupId, recipient);
        if (creator.equals(group.getCreator().getUsername())) {
            group.setUnreadByRecipient(true);
        } else {
            group.setUnreadByCreator(true);
        }
        final Message message = new Message();
        message.setCreator(author);
        message.setMessage(text);
        message.setCreated(LocalDateTime.now());
        message.setGroup(group);
        message.setUnread(true);
        messageRepository.save(message);
        return group.getId();
    }

    @Transactional
    public Long getGroupByRecipient(final String recipientId, final String authorId) {
        checkCredentials(authorId);
        final User author = authorRepository.findById(authorId).orElseThrow(() -> new ObjectNotFoundException("Author of the message is not found"));
        final User recipient = authorRepository.findById(recipientId).orElseThrow(() -> new ObjectNotFoundException("Recipient is not found"));
        final ChatGroup group = getGroupByRecipient(recipient, author);
        return Optional.ofNullable(group).map(chatGroup -> chatGroup.getId()).orElse(null);
    }

    public String getGroupName(final Long groupId, final String author) {
        final ChatGroup group = chatGroupRepository.findById(groupId).orElse(null);
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

    public long getUnreadMessagesInGroup(final String userId, final Long groupId) {
        final ChatGroup group = chatGroupRepository.findById(groupId).orElseThrow(() -> new ObjectNotFoundException("Chat group is not found"));
        return group.getMessages().parallelStream()
                .filter(message -> !message.getCreator().getUsername().equals(userId))
                .filter(message -> message.getUnread())
                .count();
    }

    public long getUnreadMessagesFromUser(final String userId) {
        checkCredentials(userId);
        return messageRepository.getUnreadMessages(userId);
    }

    @Transactional
    public void markAsReadInGroup(final String userId, final Long groupId) {
        messageRepository.markAsReadInGroup(userId, groupId);

        chatGroupRepository.findById(groupId).ifPresent(group -> {
            if (userId.equals(group.getCreator().getUsername())) {
                group.setUnreadByCreator(false);
            } else {
                group.setUnreadByRecipient(false);
            }
        });
    }

    private ChatGroup getGroupByRecipient(final User recipient, final User author) {
        ChatGroup result = null;
        final boolean friend = Optional.ofNullable(friendshipRepository.getFriendByName(author.getUsername(), recipient.getUsername()))
                .map(element -> true).orElse(false);
        if (recipient != null && friend) {
            ChatGroup group = getChatGroupFromRecipient(recipient);
            if (group != null) {
                return group;
            }
            group = getChatGroupFromRecipient(author);
            if (group != null) {
                return group;
            }
            group = createChatGroup(author, recipient);
            result = group;
        }
        return result;
    }

    private ChatGroup getChatGroupFromRecipient(final User recipient) {
        return recipient.getChatGroups().stream()
                .filter(group -> group.getPrimaryRecipient().equals(recipient))
                .findAny()
                .orElseGet(() -> null);
    }

    private ChatGroup createChatGroup(final User author, final User recipient) {
        ChatGroup group = new ChatGroup();
        group.setCreated(LocalDateTime.now());
        group.setCreator(author);
        group.setPrimaryRecipient(recipient);
        author.getChatGroups().add(group);
        recipient.getChatGroups().add(group);
        chatGroupRepository.save(group);

        return group;
    }

    private ChatGroup getChatGroup(final User author, final Long groupId, final User recipient) {
        if (groupId != null) {
            return author.getChatGroups().stream()
                    .filter(group -> group.getId() == groupId)
                    .findAny()
                    .orElseThrow(() -> new ObjectNotFoundException("Chat group is not found"));
        }
        ChatGroup group = getGroupByRecipient(recipient, author);
        if (group != null) {
            return group;
        }
        throw new RuntimeException("Undefined behavior");
    }

    private void checkCredentials(final String userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        if (!currentUser.equals(userId)) {
            throw new UnauthorizedUserException("Bad credentials");
        }
    }
}
