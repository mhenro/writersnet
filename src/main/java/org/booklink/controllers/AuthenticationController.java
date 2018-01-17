package org.booklink.controllers;

import org.booklink.models.*;
import org.booklink.models.exceptions.ObjectAlreadyExistException;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.request.Credentials;
import org.booklink.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;


/**
 * Created by mhenr on 02.10.2017.
 */
@RestController
@RequestMapping("/")
public class AuthenticationController {
    private AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @CrossOrigin
    @RequestMapping(value = "auth", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Response<String>> auth(@RequestBody Credentials credentials) {
        final String token = authenticationService.auth(credentials);
        if (token != null) {
            Response<String> response = new Response<>();
            response.setCode(0);
            response.setMessage(token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new UnauthorizedUserException();
        }
    }

    @CrossOrigin
    @RequestMapping(value = "register", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Response<String>> register(@RequestBody Credentials credentials) {
        try {
            final boolean registered = authenticationService.register(credentials);
            if (!registered) {
                throw new ObjectAlreadyExistException();
            }
            Response<String> response = new Response<>();
            response.setCode(0);
            response.setMessage("OK");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (MessagingException e) {
            Response<String> response = new Response<>();
            response.setCode(4);
            response.setMessage("Problem with sending email. Please try again later");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "activate", method = RequestMethod.GET)
    public ResponseEntity<Response<String>> activate(@RequestParam("activationToken") String activationToken) {
        Response<String> response = new Response<>();
        final boolean activated = authenticationService.activate(activationToken);
        if (activated) {
            response.setCode(0);
            response.setMessage("User activation was completed! Please log-in.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response.setCode(3);
        response.setMessage("Activation user error");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @CrossOrigin
    @RequestMapping(value = "reminder/confirm", method = RequestMethod.GET)
    public ResponseEntity<?> confirmPasswordChanging(final String email) {
        try {
            Response<String> response = new Response<>();
            authenticationService.passwordChangeConfirmation(email);
            response.setCode(0);
            response.setMessage("We sent further instructions on your email. Please, check them");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(MessagingException e) {
            Response<String> response = new Response<>();
            response.setCode(4);
            response.setMessage("Problem with sending email. Please try again later");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "reminder/password", method = RequestMethod.GET)
    public ResponseEntity<?> setDefaultPassword(final String token, final String email) {
        try {
            Response<String> response = new Response<>();
            authenticationService.setDefaultPassword(token, email);
            response.setCode(0);
            response.setMessage("Your current password was sent to the specified email");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (MessagingException e) {
            Response<String> response = new Response<>();
            response.setCode(5);
            response.setMessage("Problem with sending email. Please try again later");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* -----------------------------exception handlers------------------------------------- */

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<Response<String>> unauthorizedUser(UnauthorizedUserException e) {
        Response<String> response = new Response<>();
        response.setCode(1);
        response.setMessage("Bad credentials");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ObjectAlreadyExistException.class)
    public ResponseEntity<Response<String>> objectAlreadyExist(ObjectAlreadyExistException e) {
        Response<String> response = new Response<>();
        response.setCode(2);
        response.setMessage("Object already exist");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Response<String>> objectNotFound(ObjectNotFoundException e) {
        Response<String> response = new Response<>();
        response.setCode(3);
        response.setMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
