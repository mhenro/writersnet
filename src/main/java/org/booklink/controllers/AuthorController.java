package org.booklink.controllers;

import org.booklink.models.Response;
import org.booklink.models.exceptions.IsNotPremiumUserException;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.request.AuthorRequest;
import org.booklink.models.request.AvatarRequest;
import org.booklink.models.request.ChangePasswordRequest;
import org.booklink.models.response.*;
import org.booklink.services.AuthorService;
import org.booklink.services.SessionService;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.booklink.utils.SecurityHelper.generateActivationToken;


/**
 * Created by mhenr on 16.10.2017.
 */
@RestController
public class AuthorController {
    private AuthorService authorService;
    private SessionService sessionService;
    private Environment environment;

    @Autowired
    public AuthorController(final AuthorService authorService, final SessionService sessionService, final Environment environment) {
        this.authorService = authorService;
        this.sessionService = sessionService;
        this.environment = environment;
    }

    @CrossOrigin
    @RequestMapping(value = "authors", method = RequestMethod.GET)
    public Page<AuthorShortInfoResponse> getAuthors(Pageable pageable) {
        return authorService.getAuthors(pageable);
    }

    @CrossOrigin
    @RequestMapping(value = "count/authors", method = RequestMethod.GET)
    public ResponseEntity<?> getAuthorsCount() {
        final long count = authorService.getAuthorsCount();
        return Response.createResponseEntity(0, count, null, HttpStatus.OK);
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
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "authors", method = RequestMethod.POST)
    public ResponseEntity<?> saveAuthor(@RequestBody AuthorRequest author) {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        authorService.updateAuthor(author);
        return Response.createResponseEntity(0, "Author was saved", token, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "authors/password", method = RequestMethod.POST)
    public ResponseEntity<?> changePassword(@RequestBody final ChangePasswordRequest request) {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        authorService.changePassword(request);
        return Response.createResponseEntity(0, "Your password was changed", token, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "avatar", method = RequestMethod.POST)
    public ResponseEntity<?> saveAvatar(AvatarRequest avatarRequest) throws IOException {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        authorService.saveAvatar(avatarRequest);
        return Response.createResponseEntity(0, "Avatar was saved successfully", token, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "avatar/restore", method = RequestMethod.GET)
    public ResponseEntity<?> restoreDefaultAvatar() {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        authorService.restoreDefaultAvatar();
        return Response.createResponseEntity(0, "Avatar was restored successfully", token, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "authors/subscribe", method = RequestMethod.POST)
    public ResponseEntity<?> subscribeOnUser(@RequestBody final String subscriptionId) {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        Response<String> response = authorService.subscribeOnUser(StringUtils.strip(subscriptionId, "\""));  //remove first and last \" characters
        response.setToken(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "authors/unsubscribe", method = RequestMethod.POST)
    public ResponseEntity<?> removeSubscription(@RequestBody final String subscriptionId) {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        Response<String> response = authorService.removeSubscription(StringUtils.strip(subscriptionId, "\""));  //remove first and last \" characters
        response.setToken(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "authors/{userId:.+}/groups", method = RequestMethod.GET)
    public Page<ChatGroupResponse> getChatGroups(@PathVariable final String userId, final Pageable pageable) {
        return authorService.getChatGroups(userId, pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "friends/{authorId:.+}", method = RequestMethod.GET)
    public ResponseEntity<?> isFriendOf(@PathVariable String authorId) {
        final boolean result = authorService.isFriendOf(authorId);
        return Response.createResponseEntity(0, result, null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "subscribers/{authorId:.+}", method = RequestMethod.GET)
    public ResponseEntity<?> isSubscriberOf(@PathVariable String authorId) {
        final boolean result = authorService.isSubscriberOf(authorId);
        return Response.createResponseEntity(0, result, null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "subscriptions/{authorId:.+}", method = RequestMethod.GET)
    public ResponseEntity<?> isSubscriptionOf(@PathVariable String authorId) {
        final boolean result = authorService.isSubscriptionOf(authorId);
        return Response.createResponseEntity(0, result, null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "friendship/{authorId:.+}", method = RequestMethod.GET)
    public ResponseEntity<?> checkFriendshipWith(@PathVariable String authorId) {
        final CheckFriendshipResponse result = authorService.checkFriendshipWith(authorId);
        return Response.createResponseEntity(0, result, null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "friendship/friends/{authorId:.+}", method = RequestMethod.GET)
    public Page<FriendshipResponse> getAllFriends(@PathVariable String authorId, final Pageable pageable) {
        return authorService.getAllFriends(authorId, pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "friendship/friends/{authorId:.+}/{matcher}", method = RequestMethod.GET)
    public Page<FriendResponse> getAllFriendsByName(@PathVariable final String authorId, @PathVariable final String matcher, final Pageable pageable) {
        return authorService.getAllFriendsByName(authorId, matcher, pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "friendship/subscribers/{authorId:.+}", method = RequestMethod.GET)
    public Page<FriendshipResponse> getAllSubscribers(@PathVariable String authorId, final Pageable pageable) {
        return authorService.getAllSubscribers(authorId, pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "friendship/subscriptions/{authorId:.+}", method = RequestMethod.GET)
    public Page<FriendshipResponse> getAllSubscriptions(@PathVariable String authorId, final Pageable pageable) {
        return authorService.getAllSubscriptions(authorId, pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "friendship/new/friends/{authorId:.+}", method = RequestMethod.GET)
    public ResponseEntity<?> getNewFriendsCount(@PathVariable String authorId) {
        final long count = authorService.getNewFriendsCount(authorId);
        return Response.createResponseEntity(0, count, null, HttpStatus.OK);
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

    @ExceptionHandler(IsNotPremiumUserException.class)
    public ResponseEntity<?> isNotPremiumUser(IsNotPremiumUserException e) {
        return Response.createResponseEntity(6, e.getMessage().isEmpty() ? "Only a premium user can do this" : e.getMessage(), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> ioException(IOException e) {
        return Response.createResponseEntity(7, "Problem with server's file system. Please try again later. Reason: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
