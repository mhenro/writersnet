package org.booklink.controllers;

import org.booklink.models.*;
import org.booklink.models.exceptions.ObjectAlreadyExistException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.request_models.Credentials;
import org.booklink.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
        final boolean registered = authenticationService.register(credentials);
        if (!registered) {
            throw new ObjectAlreadyExistException();
        }
        Response<String> response = new Response<>();
        response.setCode(0);
        response.setMessage("OK");
        return new ResponseEntity<>(response, HttpStatus.OK);
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
}
