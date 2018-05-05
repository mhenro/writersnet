package com.writersnets.controllers;

import com.writersnets.models.Response;
import com.writersnets.models.exceptions.ObjectNotFoundException;
import com.writersnets.models.exceptions.UnauthorizedUserException;
import com.writersnets.models.exceptions.WrongDataException;
import com.writersnets.models.request.CommentRequest;
import com.writersnets.models.response.CommentResponse;
import com.writersnets.models.response.DetailedCommentResponse;
import com.writersnets.services.CommentsService;
import com.writersnets.services.SessionService;
import com.writersnets.utils.ControllerHelper;
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
 * Created by mhenr on 15.11.2017.
 */
@RestController
@CrossOrigin
public class CommentsController {
    private CommentsService commentsService;
    private SessionService sessionService;
    private Environment environment;

    @Autowired
    public CommentsController(final CommentsService commentsService, final SessionService sessionService, final Environment environment) {
        this.commentsService = commentsService;
        this.sessionService = sessionService;
        this.environment = environment;
    }

    @RequestMapping(value = "books/{bookId}/comments", method = RequestMethod.GET)
    public Page<CommentResponse> getComments(@PathVariable Long bookId, Pageable pageable) {
        return commentsService.getComments(bookId, pageable);
    }

    @RequestMapping(value = "books/comments", method = RequestMethod.GET)
    public Page<DetailedCommentResponse> getCommentsGroupByBookOrderByDate(final Pageable pageable) {
        return commentsService.getCommentsGroupByBookOrderByDate(pageable);
    }

    @RequestMapping(value = "books/comments", method = RequestMethod.POST)
    public ResponseEntity<?> saveComment(@RequestBody CommentRequest bookComment) {
        commentsService.saveComment(bookComment);
        return Response.createResponseEntity(0, "Your comment was added", null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "books/{bookId}/comments/{commentId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteComment(@PathVariable Long bookId, @PathVariable Long commentId) {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        commentsService.deleteComment(bookId, commentId);
        return Response.createResponseEntity(0, "Your comment was deleted", token, HttpStatus.OK);
    }

    /* ----------------------------------------exception handlers*------------------------------------------ */

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<?> unauthorizedUser(UnauthorizedUserException e) {
        return Response.createResponseEntity(1, ControllerHelper.getErrorOrDefaultMessage(e, "Forbidden"), null, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> objectNotFound(ObjectNotFoundException e) {
        return Response.createResponseEntity(5, ControllerHelper.getErrorOrDefaultMessage(e, "Object is not found"), null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WrongDataException.class)
    public ResponseEntity<?> wrongData(WrongDataException e) {
        return Response.createResponseEntity(6, ControllerHelper.getErrorOrDefaultMessage(e, "Wrong data"), null, HttpStatus.BAD_REQUEST);
    }
}