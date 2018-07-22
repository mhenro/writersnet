package com.writersnets.controllers;

import com.writersnets.models.*;
import com.writersnets.models.request.Credentials;
import com.writersnets.services.AuthenticationService;
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
        authenticationService.setDefaultPassword(token, email);
        return Response.createResponseEntity(0, "Your current password was sent to the specified email", null, HttpStatus.OK);
    }
}
