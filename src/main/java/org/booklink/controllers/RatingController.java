package org.booklink.controllers;

import org.booklink.models.Response;
import org.booklink.models.entities.Rating;
import org.booklink.models.entities.RatingId;
import org.booklink.models.exceptions.ObjectAlreadyExistException;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.repositories.RatingRepository;
import org.booklink.services.RatingService;
import org.booklink.utils.ControllerHelper;
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

     /* ---------------------------------------exception handlers-------------------------------------- */

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> objectNotFound(ObjectNotFoundException e) {
        return Response.createResponseEntity(5, ControllerHelper.getErrorOrDefaultMessage(e, "Object is not found"), null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ObjectAlreadyExistException.class)
    public ResponseEntity<?> objectAlreadyExist(ObjectAlreadyExistException e) {
        return Response.createResponseEntity(5, ControllerHelper.getErrorOrDefaultMessage(e, "Object already exist"), null, HttpStatus.BAD_REQUEST);
    }
}
