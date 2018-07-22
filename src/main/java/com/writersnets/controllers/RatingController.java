package com.writersnets.controllers;

import com.writersnets.models.Response;
import com.writersnets.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by mhenr on 15.11.2017.
 */
@RestController
@CrossOrigin
public class RatingController {
    private RatingService ratingService;

    @Autowired
    public RatingController(final RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @RequestMapping(value = "books/{bookId}/rating/{value}", method = RequestMethod.GET)
    public ResponseEntity<?> addStar(@PathVariable Long bookId, @PathVariable Integer value, HttpServletRequest request) {
        ratingService.addStar(bookId, value, request);
        return Response.createResponseEntity(0, "Your vote was added", null, HttpStatus.OK);
    }
}
