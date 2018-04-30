package com.writersnets.controllers;

import com.writersnets.models.Response;
import com.writersnets.models.exceptions.ObjectNotFoundException;
import com.writersnets.models.exceptions.UnauthorizedUserException;
import com.writersnets.models.request.SerieRequest;
import com.writersnets.models.response.BookSerieResponse;
import com.writersnets.services.SerieService;
import com.writersnets.services.SessionService;
import com.writersnets.utils.ControllerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.writersnets.utils.SecurityHelper.generateActivationToken;

/**
 * Created by mhenr on 15.11.2017.
 */
@RestController
@CrossOrigin
public class SerieController {
    private SerieService serieService;
    private SessionService sessionService;
    private Environment environment;

    @Autowired
    public SerieController(final SerieService serieService, final SessionService sessionService, final Environment environment) {
        this.serieService = serieService;
        this.sessionService = sessionService;
        this.environment = environment;
    }

    @RequestMapping(value = "series/{userId:.+}", method = RequestMethod.GET)
    public Page<BookSerieResponse> getBookSeries(@PathVariable String userId, Pageable pageable) {
        return serieService.getBookSeries(userId, pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "series", method = RequestMethod.POST)
    public ResponseEntity<?> saveSerie(@RequestBody SerieRequest serie) {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        Long serieId = serieService.saveSerie(serie);
        return Response.createResponseEntity(0, String.valueOf(serieId), token, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "series/{serieId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSerie(@PathVariable Long serieId) {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        serieService.deleteSerie(serieId);
        return Response.createResponseEntity(0, "Serie was deleted successfully", token, HttpStatus.OK);
    }

     /* ---------------------------------------exception handlers-------------------------------------- */

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<?> unauthorizedUser(UnauthorizedUserException e) {
        return Response.createResponseEntity(1, ControllerHelper.getErrorOrDefaultMessage(e, "Bad credentials"), null, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> objectNotFound(ObjectNotFoundException e) {
        return Response.createResponseEntity(5, ControllerHelper.getErrorOrDefaultMessage(e, "Object is not found"), null, HttpStatus.NOT_FOUND);
    }
}
