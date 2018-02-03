package org.booklink.controllers;

import org.booklink.models.Response;
import org.booklink.models.entities.ChatGroup;
import org.booklink.models.entities.Message;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.request.MessageRequest;
import org.booklink.models.request.ReadMessageRequest;
import org.booklink.models.response.MessageResponse;
import org.booklink.services.MessageService;
import org.booklink.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.booklink.utils.SecurityHelper.generateActivationToken;

/**
 * Created by mhenr on 07.12.2017.
 */
@RestController
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
    @CrossOrigin
    @RequestMapping(value = "{userId:.+}/messages/{groupId}", method = RequestMethod.GET)
    public Page<MessageResponse> getMessagesByGroup(@PathVariable final String userId, @PathVariable final Long groupId, final Pageable pageable) {
        return messageService.getMessagesByGroup(userId, groupId, pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "messages/add", method = RequestMethod.POST)
    public ResponseEntity<?> addMessageToGroup(@RequestBody final MessageRequest message) {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        final Long groupId = messageService.addMessageToGroup(message.getCreator(), message.getPrimaryRecipient(), message.getText(), message.getGroupId());
        return Response.createResponseEntity(0, groupId, token, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "groups/get", method = RequestMethod.POST)
    public ResponseEntity<?> getGroupIdFromRecipient(@RequestBody final MessageRequest messageRequest) {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        Long groupId = messageService.getGroupByRecipient(messageRequest.getPrimaryRecipient(), messageRequest.getCreator());
        return Response.createResponseEntity(0, groupId, token, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "groups/{groupId}/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getGroupName(@PathVariable final Long groupId, @PathVariable final String userId) {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        return Response.createResponseEntity(0, messageService.getGroupName(groupId, userId), token, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "{userId}/messages/unread", method = RequestMethod.GET)
    public ResponseEntity<?> getUnreadMessagesFromUser(@PathVariable final String userId) {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        return Response.createResponseEntity(0, messageService.getUnreadMessagesFromUser(userId), token, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "groups/{groupId}/{userId}/messages/unread", method = RequestMethod.GET)
    public ResponseEntity<?> getUnreadMessagesInGroup(@PathVariable final Long groupId, @PathVariable final String userId) {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        return Response.createResponseEntity(0, messageService.getUnreadMessagesInGroup(userId, groupId), token, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "groups/messages/read", method = RequestMethod.POST)
    public ResponseEntity<?> markAllAsReadInGroup(@RequestBody final ReadMessageRequest readMessageRequest) {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        messageService.markAsReadInGroup(readMessageRequest.getUserId(), readMessageRequest.getGroupId());
        return Response.createResponseEntity(0, "All messages are read", token, HttpStatus.OK);
    }

    /* ---------------------------------------exception handlers-------------------------------------- */

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<?> unauthorizedUser(UnauthorizedUserException e) {
        return Response.createResponseEntity(1, e.getMessage().isEmpty() ? "Bad credentials" : e.getMessage(), null, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> userNotFound(ObjectNotFoundException e) {
        return Response.createResponseEntity(5, e.getMessage().isEmpty() ? "User is not found" : e.getMessage(), null, HttpStatus.NOT_FOUND);
    }
}
