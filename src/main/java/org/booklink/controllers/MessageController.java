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
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public MessageController(final MessageService messageService) {
        this.messageService = messageService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "{userId:.+}/messages/{groupId}", method = RequestMethod.GET)
    public Page<MessageResponse> getMessagesByGroup(@PathVariable String userId, @PathVariable Long groupId, Pageable pageable) {
        return messageService.getMessagesByGroup(userId, groupId, pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "messages/add", method = RequestMethod.POST)
    public ResponseEntity<?> addMessageToGroup(@RequestBody MessageRequest message) {
        String token = generateActivationToken();
        final Long groupId;
        try {
            groupId = messageService.addMessageToGroup(message.getCreator(), message.getPrimaryRecipient(), message.getText(), message.getGroupId());
        } catch (Exception e) {
            Response<String> response = new Response<>();
            response.setCode(1);
            response.setMessage(e.getMessage());
            response.setToken(token);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Response<Long> response = new Response<>();
        response.setCode(0);
        response.setMessage(groupId);
        response.setToken(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "groups/get", method = RequestMethod.POST)
    public ResponseEntity<?> getGroupIdFromRecipient(@RequestBody MessageRequest messageRequest) {
        String token = generateActivationToken();
        Long groupId = null;
        try {
            ChatGroup group = messageService.getGroupByRecipient(messageRequest.getPrimaryRecipient(), messageRequest.getCreator());
            if (group != null) {
                groupId = group.getId();
            }
        } catch(Exception e) {
            Response<String> response = new Response<>();
            response.setCode(1);
            response.setMessage(e.getMessage());
            response.setToken(token);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Response<Long> response = new Response<>();
        response.setCode(0);
        response.setMessage(groupId);
        response.setToken(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "groups/{groupId}/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getGroupName(@PathVariable final Long groupId, @PathVariable final String userId) {
        Response<String> response = new Response<>();
        String token = generateActivationToken();
        try {
            response.setCode(0);
            response.setMessage(messageService.getGroupName(groupId, userId));
            response.setToken(token);
        } catch(Exception e) {
            response.setCode(1);
            response.setMessage(e.getMessage());
            response.setToken(token);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "{userId}/messages/unread", method = RequestMethod.GET)
    public ResponseEntity<?> getUnreadMessagesFromUser(@PathVariable final String userId) {
        String token = generateActivationToken();
        try {
            Response<Long> response = new Response<>();
            response.setCode(0);
            response.setMessage(messageService.getUnreadMessagesFromUser(userId));
            response.setToken(token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Response<String> response = new Response<>();
            response.setCode(1);
            response.setMessage(e.getMessage());
            response.setToken(token);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "groups/{groupId}/{userId}/messages/unread", method = RequestMethod.GET)
    public ResponseEntity<?> getUnreadMessagesInGroup(@PathVariable final Long groupId, @PathVariable final String userId) {
        String token = generateActivationToken();
        try {
            Response<Long> response = new Response<>();
            response.setCode(0);
            response.setMessage(messageService.getUnreadMessagesInGroup(userId, groupId));
            response.setToken(token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Response<String> response = new Response<>();
            response.setCode(1);
            response.setMessage(e.getMessage());
            response.setToken(token);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "groups/messages/read", method = RequestMethod.POST)
    public ResponseEntity<?> markAllAsReadInGroup(@RequestBody ReadMessageRequest readMessageRequest) {
        Response<String> response = new Response<>();
        String token = generateActivationToken();
        try {
            messageService.markAsReadInGroup(readMessageRequest.getUserId(), readMessageRequest.getGroupId());
            response.setCode(0);
            response.setMessage("All messages are read");
            response.setToken(token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setCode(1);
            response.setMessage(e.getMessage());
            response.setToken(token);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* ---------------------------------------exception handlers-------------------------------------- */

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<?> unauthorizedUser(UnauthorizedUserException e) {
        Response<String> response = new Response<>();
        response.setCode(1);
        response.setMessage("Bad credentials");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> userNotFound(ObjectNotFoundException e) {
        Response<String> response = new Response<>();
        response.setCode(5);
        response.setMessage("User not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
