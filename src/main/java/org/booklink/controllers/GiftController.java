package org.booklink.controllers;

import org.booklink.models.Response;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.response.GiftResponse;
import org.booklink.services.GiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by mhenr on 27.01.2018.
 */
@RestController
public class GiftController {
    private GiftService giftService;

    @Autowired
    public GiftController(final GiftService giftService) {
        this.giftService = giftService;
    }

    @CrossOrigin
    @RequestMapping(value = "gift/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getGift(@PathVariable final Long id) {
        final Response<GiftResponse> response = new Response<>();
        final GiftResponse gift = giftService.getGift(id);
        response.setCode(0);
        response.setMessage(gift);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /* ---------------------------------------exception handlers-------------------------------------- */

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> objectNotFound(ObjectNotFoundException e) {
        Response<String> response = new Response<>();
        response.setCode(5);
        response.setMessage(e.getMessage().isEmpty() ? "Object not found" : e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
