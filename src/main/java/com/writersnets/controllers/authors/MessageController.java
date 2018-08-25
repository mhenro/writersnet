package com.writersnets.controllers.authors;

import com.writersnets.models.Response;
import com.writersnets.models.request.MessageRequest;
import com.writersnets.models.request.ReadMessageRequest;
import com.writersnets.models.response.MessageResponse;
import com.writersnets.services.authors.MessageService;
import com.writersnets.services.security.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.writersnets.utils.SecurityHelper.generateActivationToken;

/**
 * Created by mhenr on 07.12.2017.
 */
@RestController
@CrossOrigin
public class MessageController {
    private MessageService messageService;
    private SessionService sessionService;
    private Environment environment;

    @Autowired
    public MessageController(final MessageService messageService, final SessionService sessionService, final Environment environment) {
        this.messageService = messageService;
        this.sessionService = sessionService;
        this.environment = environment;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "{userId:.+}/messages/{groupId}", method = RequestMethod.GET)
    public Page<MessageResponse> getMessagesByGroup(@PathVariable final String userId, @PathVariable final Long groupId, final Pageable pageable) {
        return messageService.getMessagesByGroup(userId, groupId, pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "messages/add", method = RequestMethod.POST)
    public ResponseEntity<?> addMessageToGroup(@RequestBody final MessageRequest message) {
        final String key = environment.getProperty("security.jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        final Long groupId = messageService.addMessageToGroup(message.getCreator(), message.getPrimaryRecipient(), message.getText(), message.getGroupId());
        return Response.createResponseEntity(0, groupId, token, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "groups/get", method = RequestMethod.POST)
    public ResponseEntity<?> getGroupIdFromRecipient(@RequestBody final MessageRequest messageRequest) {
        final String key = environment.getProperty("security.jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        Long groupId = messageService.getGroupByRecipient(messageRequest.getPrimaryRecipient(), messageRequest.getCreator());
        return Response.createResponseEntity(0, groupId, token, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "groups/{groupId}/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getGroupName(@PathVariable final Long groupId, @PathVariable final String userId) {
        final String key = environment.getProperty("security.jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        return Response.createResponseEntity(0, messageService.getGroupName(groupId, userId), token, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "{userId}/messages/unread", method = RequestMethod.GET)
    public ResponseEntity<?> getUnreadMessagesFromUser(@PathVariable final String userId) {
        final String key = environment.getProperty("security.jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        return Response.createResponseEntity(0, messageService.getUnreadMessagesFromUser(userId), token, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "groups/{groupId}/{userId}/messages/unread", method = RequestMethod.GET)
    public ResponseEntity<?> getUnreadMessagesInGroup(@PathVariable final Long groupId, @PathVariable final String userId) {
        final String key = environment.getProperty("security.jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        return Response.createResponseEntity(0, messageService.getUnreadMessagesInGroup(userId, groupId), token, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "groups/messages/read", method = RequestMethod.POST)
    public ResponseEntity<?> markAllAsReadInGroup(@RequestBody final ReadMessageRequest readMessageRequest) {
        final String key = environment.getProperty("security.jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        messageService.markAsReadInGroup(readMessageRequest.getUserId(), readMessageRequest.getGroupId());
        return Response.createResponseEntity(0, "All messages are read", token, HttpStatus.OK);
    }
}
