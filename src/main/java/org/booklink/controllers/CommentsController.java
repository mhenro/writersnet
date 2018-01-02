package org.booklink.controllers;

import org.booklink.models.Response;
import org.booklink.models.entities.Session;
import org.booklink.models.request.CommentRequest;
import org.booklink.models.response.CommentResponse;
import org.booklink.services.CommentsService;
import org.booklink.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.booklink.utils.SecurityHelper.generateActivationToken;

/**
 * Created by mhenr on 15.11.2017.
 */
@RestController
public class CommentsController {
    private CommentsService commentsService;
    private SessionService sessionService;

    @Autowired
    public CommentsController(final CommentsService commentsService, final SessionService sessionService) {
        this.commentsService = commentsService;
        this.sessionService = sessionService;
    }

    @CrossOrigin
    @RequestMapping(value = "books/{bookId}/comments", method = RequestMethod.GET)
    public Page<CommentResponse> getComments(@PathVariable Long bookId, Pageable pageable) {
        return commentsService.getComments(bookId, pageable);
    }

    @CrossOrigin
    @RequestMapping(value = "books/comments", method = RequestMethod.POST)
    public ResponseEntity<?> saveComment(@RequestBody CommentRequest bookComment) {
        Response<String> response = new Response<>();
        try {
            commentsService.saveComment(bookComment);
        } catch (Exception e) {
            response.setCode(1);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.setCode(0);
        response.setMessage("Your comment was added");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "books/{bookId}/comments/{commentId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteComment(@PathVariable Long bookId, @PathVariable Long commentId) {
        Response<String> response = new Response<>();
        String token = generateActivationToken();
        sessionService.updateSession(token);
        try {
            commentsService.deleteComment(bookId, commentId);
        } catch (Exception e) {
            response.setCode(1);
            response.setMessage(e.getMessage());
            response.setToken(token);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.setCode(0);
        response.setMessage("Your comment was deleted");
        response.setToken(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
