package org.booklink.controllers;

import org.booklink.models.Response;
import org.booklink.models.entities.User;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.request_models.AvatarRequest;
import org.booklink.models.response_models.AuthorResponse;
import org.booklink.models.response_models.AuthorShortInfoResponse;
import org.booklink.models.response_models.ChatGroupResponse;
import org.booklink.models.response_models.FriendResponse;
import org.booklink.services.AuthorService;
import org.booklink.services.SessionService;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.booklink.utils.SecurityHelper.generateActivationToken;


/**
 * Created by mhenr on 16.10.2017.
 */
@RestController
public class AuthorController {
    private AuthorService authorService;
    private SessionService sessionService;

    @Autowired
    public AuthorController(final AuthorService authorService, final SessionService sessionService) {
        this.authorService = authorService;
        this.sessionService = sessionService;
    }

    @CrossOrigin
    @RequestMapping(value = "authors", method = RequestMethod.GET)
    public Page<AuthorShortInfoResponse> getAuthors(Pageable pageable) {
        return authorService.getAuthors(pageable);
    }

    @CrossOrigin
    @RequestMapping(value = "authors/name/{authorName:.+}", method = RequestMethod.GET)
    public Page<AuthorShortInfoResponse> getAuthorsByName(@PathVariable final String authorName, final Pageable pageable) {
        return authorService.getAuthorsByName(authorName, pageable);
    }

    @CrossOrigin
    @RequestMapping(value = "authors/{authorId:.+}", method = RequestMethod.GET)
    public ResponseEntity<?> getAuthor(@PathVariable String authorId) {
        AuthorResponse author = authorService.getAuthor(authorId);
        if (author != null) {
            return new ResponseEntity<>(author, HttpStatus.OK);
        }
        Response<String> response = new Response<>();
        response.setCode(1);
        response.setMessage("Author not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "authors", method = RequestMethod.POST)
    public ResponseEntity<?> saveAuthor(@RequestBody User author) {
        Response<String> response = new Response<>();
        String token = generateActivationToken();
        sessionService.updateSession(token);
        try {
            authorService.saveAuthor(author);
        } catch(Exception e) {
            response.setCode(1);
            response.setMessage(e.getMessage());
            response.setToken(token);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.setCode(0);
        response.setMessage("Author was saved");
        response.setToken(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "avatar", method = RequestMethod.POST)
    public ResponseEntity<?> saveAvatar(AvatarRequest avatarRequest) {
        Response<String> response = new Response<>();
        String token = generateActivationToken();
        try {
            authorService.saveAvatar(avatarRequest);
            response.setCode(0);
            response.setMessage("Avatar was saved successfully");
            response.setToken(token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(Exception e) {
            response.setCode(1);
            response.setMessage(e.getMessage());
            response.setToken(token);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "authors/subscribe", method = RequestMethod.POST)
    public ResponseEntity<?> subscribeOnUser(@RequestBody final String subscriptionId) {
        Response<String> response = new Response<>();
        String token = generateActivationToken();
        try {
            response = authorService.subscribeOnUser(StringUtils.strip(subscriptionId, "\""));  //remove first and last \" characters
            response.setToken(token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(Exception e) {
            response.setCode(1);
            response.setMessage(e.getMessage());
            response.setToken(token);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "authors/unsubscribe", method = RequestMethod.POST)
    public ResponseEntity<?> removeSubscription(@RequestBody final String subscriptionId) {
        Response<String> response = new Response<>();
        String token = generateActivationToken();
        try {
            response = authorService.removeSubscription(StringUtils.strip(subscriptionId, "\""));  //remove first and last \" characters
            response.setToken(token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(Exception e) {
            response.setCode(1);
            response.setMessage(e.getMessage());
            response.setToken(token);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "authors/{userId:.+}/groups", method = RequestMethod.GET)
    public Page<ChatGroupResponse> getChatGroups(@PathVariable final String userId, final Pageable pageable) {
        return authorService.getChatGroups(userId, pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "friends/{userId:.+}/{matcher}", method = RequestMethod.GET)
    public Page<FriendResponse> getFriends(@PathVariable final String userId, @PathVariable final String matcher, final Pageable pageable) {
        return authorService.getFriends(userId, matcher, pageable);
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
