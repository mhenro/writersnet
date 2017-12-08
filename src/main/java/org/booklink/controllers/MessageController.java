package org.booklink.controllers;

import org.booklink.models.Response;
import org.booklink.models.entities.Message;
import org.booklink.models.entities.User;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.request_models.MessageRequest;
import org.booklink.repositories.MessageRepository;
import org.booklink.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public Page<Message> getMessagesByGroup(@PathVariable String userId, @PathVariable Long groupId, Pageable pageable) {
        return messageService.getMessagesByGroup(userId, groupId, pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "messages/add", method = RequestMethod.POST)
    public ResponseEntity<?> addMessageToGroup(@RequestBody MessageRequest message) {
        Response<String> response = new Response<>();
        try {
            messageService.addMessageToGroup(message.getCreator(), message.getText(), message.getGroupId());
        } catch (Exception e) {
            response.setCode(1);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.setCode(0);
        response.setMessage("Message was successfully added to chat");
        return new ResponseEntity<>(response, HttpStatus.OK);
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
