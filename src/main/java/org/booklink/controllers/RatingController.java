package org.booklink.controllers;

import org.booklink.models.Response;
import org.booklink.models.entities.Rating;
import org.booklink.models.entities.RatingId;
import org.booklink.models.exceptions.ObjectAlreadyExistException;
import org.booklink.repositories.RatingRepository;
import org.booklink.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by mhenr on 15.11.2017.
 */
@RestController
public class RatingController {
    private RatingService ratingService;

    @Autowired
    public RatingController(final RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @CrossOrigin
    @RequestMapping(value = "books/{bookId}/rating/{value}", method = RequestMethod.GET)
    public ResponseEntity<?> addStar(@PathVariable Long bookId, @PathVariable Integer value, HttpServletRequest request) {
        Response<String> response = new Response<>();
        try {
            ratingService.addStar(bookId, value, request);
        } catch(Exception e) {
            response.setCode(1);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.setCode(0);
        response.setMessage("Your vote was added");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
