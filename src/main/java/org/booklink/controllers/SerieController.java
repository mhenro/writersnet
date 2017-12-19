package org.booklink.controllers;

import org.booklink.models.Response;
import org.booklink.models.entities.BookSerie;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.request_models.Serie;
import org.booklink.repositories.SerieRepository;
import org.booklink.services.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static org.booklink.utils.SecurityHelper.generateActivationToken;

/**
 * Created by mhenr on 15.11.2017.
 */
@RestController
public class SerieController {
    private SerieService serieService;

    @Autowired
    public SerieController(final SerieService serieService) {
        this.serieService = serieService;
    }

    @CrossOrigin
    @RequestMapping(value = "series/{userId:.+}", method = RequestMethod.GET)
    public Page<BookSerie> getBookSeries(@PathVariable String userId, Pageable pageable) {
        return serieService.getBookSeries(userId, pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "series", method = RequestMethod.POST)
    public ResponseEntity<?> saveSerie(@RequestBody Serie serie) {
        Response<String> response = new Response<>();
        String token = generateActivationToken();
        Long serieId = null;
        try {
            serieId = serieService.saveSerie(serie);
        } catch(Exception e) {
            response.setCode(1);
            response.setMessage(e.getMessage());
            response.setToken(token);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.setCode(0);
        response.setMessage(String.valueOf(serieId));
        response.setToken(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "series/{serieId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSerie(@PathVariable Long serieId) {
        Response<String> response = new Response<>();
        String token = generateActivationToken();
        try {
            serieService.deleteSerie(serieId);
        }  catch(Exception e) {
            response.setCode(1);
            response.setMessage(e.getMessage());
            response.setToken(token);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.setCode(0);
        response.setMessage("Serie was deleted successfully");
        response.setToken(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
