package com.writersnets.controllers;

import com.writersnets.models.*;
import com.writersnets.models.exceptions.ObjectAlreadyExistException;
import com.writersnets.models.exceptions.ObjectNotFoundException;
import com.writersnets.models.exceptions.UnauthorizedUserException;
import com.writersnets.models.request.Credentials;
import com.writersnets.services.AuthenticationService;
import com.writersnets.utils.ControllerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;


/**
 * Created by mhenr on 02.10.2017.
 */
@RestController
@CrossOrigin
@RequestMapping("/")
public class AuthenticationController {
    private AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @RequestMapping(value = "auth", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Response<String>> auth(@RequestBody final Credentials credentials) {
        final String token = authenticationService.auth(credentials);
        return Response.createResponseEntity(0, token, null, HttpStatus.OK);
    }

    @RequestMapping(value = "register", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Response<String>> register(@RequestBody final Credentials credentials) throws MessagingException {
        authenticationService.register(credentials);
        return Response.createResponseEntity(0, "OK", null, HttpStatus.OK);
    }

    @RequestMapping(value = "activate", method = RequestMethod.GET)
    public ResponseEntity<Response<String>> activate(@RequestParam("activationToken") final String activationToken) {
        authenticationService.activate(activationToken);
        return Response.createResponseEntity(0, "User activation was completed! Please log-in.", null, HttpStatus.OK);
    }

    @RequestMapping(value = "reminder/confirm", method = RequestMethod.GET)
    public ResponseEntity<?> confirmPasswordChanging(final String email) throws MessagingException {
        authenticationService.passwordChangeConfirmation(email);
        return Response.createResponseEntity(0, "We sent further instructions on your email. Please, check them", null, HttpStatus.OK);
    }

    @RequestMapping(value = "reminder/password", method = RequestMethod.GET)
    public ResponseEntity<?> setDefaultPassword(final String token, final String email) throws MessagingException{
        return Response.createResponseEntity(0, "Your current password was sent to the specified email", null, HttpStatus.OK);
    }

    /* -----------------------------exception handlers------------------------------------- */

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<Response<String>> unauthorizedUser(UnauthorizedUserException e) {
        return Response.createResponseEntity(1, ControllerHelper.getErrorOrDefaultMessage(e, "Bad credentials"), null, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ObjectAlreadyExistException.class)
    public ResponseEntity<Response<String>> objectAlreadyExist(ObjectAlreadyExistException e) {
        return Response.createResponseEntity(2, ControllerHelper.getErrorOrDefaultMessage(e, "Object already exist"), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Response<String>> objectNotFound(ObjectNotFoundException e) {
        return Response.createResponseEntity(3, ControllerHelper.getErrorOrDefaultMessage(e, "Object is not found"), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<Response<String>> emailException(MessagingException e) {
        return Response.createResponseEntity(4, "Problem with sending email. Please try again later. Reason: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
