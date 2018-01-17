package org.booklink.controllers;

import org.booklink.models.Response;
import org.booklink.models.entities.User;
import org.booklink.models.exceptions.IsNotPremiumUser;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping(value = "count/authors", method = RequestMethod.GET)
    public ResponseEntity<?> getAuthorsCount() {
        final long count = authorService.getAuthorsCount();
        Response<Long> response = new Response<>();
        response.setCode(0);
        response.setMessage(count);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "authors/name/{authorName:.+}", method = RequestMethod.GET)
    public Page<AuthorShortInfoResponse> getAuthorsByName(@PathVariable final String authorName, final Pageable pageable) {
        return authorService.getAuthorsByName(authorName, pageable);
    }

    @CrossOrigin
    @RequestMapping(value = "authors/letter/{authorName:.+}", method = RequestMethod.GET)
    public Page<AuthorShortInfoResponse> getAuthorsByFirstLetter(@PathVariable final String authorName, final Pageable pageable) {
        return authorService.getAuthorsByFirstLetter(authorName, pageable);
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
    public ResponseEntity<?> saveAuthor(@RequestBody AuthorRequest author) {
        Response<String> response = new Response<>();
        String token = generateActivationToken();
        sessionService.updateSession(token);
        try {
            authorService.updateAuthor(author);
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
    @RequestMapping(value = "authors/password", method = RequestMethod.POST)
    public ResponseEntity<?> changePassword(@RequestBody final ChangePasswordRequest request) {
        Response<String> response = new Response<>();
        String token = generateActivationToken();
        sessionService.updateSession(token);
        try {
            authorService.changePassword(request);
        } catch(Exception e) {
            response.setCode(1);
            response.setMessage(e.getMessage());
            response.setToken(token);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.setCode(0);
        response.setMessage("Your password was changed");
        response.setToken(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "avatar", method = RequestMethod.POST)
    public ResponseEntity<?> saveAvatar(AvatarRequest avatarRequest) {
        Response<String> response = new Response<>();
        String token = generateActivationToken();
        sessionService.updateSession(token);
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
    @RequestMapping(value = "avatar/restore", method = RequestMethod.GET)
    public ResponseEntity<?> restoreDefaultAvatar() {
        Response<String> response = new Response<>();
        String token = generateActivationToken();
        sessionService.updateSession(token);
        try {
            authorService.restoreDefaultAvatar();
            response.setCode(0);
            response.setMessage("Avatar was restored successfully");
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
        sessionService.updateSession(token);
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
        sessionService.updateSession(token);
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
    @RequestMapping(value = "friends/{authorId:.+}", method = RequestMethod.GET)
    public ResponseEntity<?> isFriendOf(@PathVariable String authorId) {
        final boolean result = authorService.isFriendOf(authorId);
        Response<Boolean> response = new Response<>();
        response.setCode(0);
        response.setMessage(result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "subscribers/{authorId:.+}", method = RequestMethod.GET)
    public ResponseEntity<?> isSubscriberOf(@PathVariable String authorId) {
        final boolean result = authorService.isSubscriberOf(authorId);
        Response<Boolean> response = new Response<>();
        response.setCode(0);
        response.setMessage(result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "subscriptions/{authorId:.+}", method = RequestMethod.GET)
    public ResponseEntity<?> isSubscriptionOf(@PathVariable String authorId) {
        final boolean result = authorService.isSubscriptionOf(authorId);
        Response<Boolean> response = new Response<>();
        response.setCode(0);
        response.setMessage(result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "friendship/{authorId:.+}", method = RequestMethod.GET)
    public ResponseEntity<?> checkFriendshipWith(@PathVariable String authorId) {
        final CheckFriendshipResponse result = authorService.checkFriendshipWith(authorId);
        Response<CheckFriendshipResponse> response = new Response<>();
        response.setCode(0);
        response.setMessage(result);
        return new ResponseEntity<>(response, HttpStatus.OK);
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
        final Response<Long> response = new Response<>();
        response.setCode(0);
        response.setMessage(count);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /* ---------------------------------------exception handlers-------------------------------------- */

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<?> unauthorizedUser(UnauthorizedUserException e) {
        Response<String> response = new Response<>();
        response.setCode(1);
        response.setMessage(e.getMessage().isEmpty() ? "Bad credentials" : e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> userNotFound(ObjectNotFoundException e) {
        Response<String> response = new Response<>();
        response.setCode(5);
        response.setMessage(e.getMessage().isEmpty() ? "User not found" : e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IsNotPremiumUser.class)
    public ResponseEntity<?> isNotPremiumUser(IsNotPremiumUser e) {
        Response<String> response = new Response<>();
        response.setCode(6);
        response.setMessage(e.getMessage().isEmpty() ? "Only a premium user can do this" : e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
