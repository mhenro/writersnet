package org.booklink.controllers;

import org.booklink.models.Response;
import org.booklink.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by mhenr on 20.12.2017.
 */
@RestController
public class SessionController {
    private SessionService sessionService;

    @Autowired
    public SessionController(final SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @CrossOrigin
    @RequestMapping(value = "count/sessions", method = RequestMethod.GET)
    public ResponseEntity<?> getSessionCount() {
        Response<Long> response = new Response<>();
        response.setCode(0);
        response.setMessage(sessionService.getOnlineUsers());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "sessions/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> isUserOnline(@PathVariable final String userId) {
        Response<Boolean> response = new Response<>();
        response.setCode(0);
        response.setMessage(sessionService.isUserOnline(userId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
