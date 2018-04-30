package com.writersnets.controllers;

import com.writersnets.models.Response;
import com.writersnets.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by mhenr on 20.12.2017.
 */
@RestController
@CrossOrigin
public class SessionController {
    private SessionService sessionService;

    @Autowired
    public SessionController(final SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @RequestMapping(value = "count/sessions", method = RequestMethod.GET)
    public ResponseEntity<?> getSessionCount() {
        return Response.createResponseEntity(0, sessionService.getOnlineUsers(), null, HttpStatus.OK);
    }

    @RequestMapping(value = "sessions/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> isUserOnline(@PathVariable final String userId) {
        return Response.createResponseEntity(0, sessionService.isUserOnline(userId), null, HttpStatus.OK);
    }
}
