package org.booklink.controllers;

import org.booklink.models.Response;
import org.booklink.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    @RequestMapping(value = "sessions/count", method = RequestMethod.GET)
    public ResponseEntity<?> getSessionCount() {
        Response<Long> response = new Response<>();
        response.setCode(0);
        response.setMessage(sessionService.getOnlineUsers());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
