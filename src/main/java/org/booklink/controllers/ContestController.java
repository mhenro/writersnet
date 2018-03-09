package org.booklink.controllers;

import org.booklink.models.Response;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.exceptions.WrongDataException;
import org.booklink.models.request.AddJudgeRequest;
import org.booklink.models.request.ContestRequest;
import org.booklink.models.response.ContestResponse;
import org.booklink.services.ContestService;
import org.bouncycastle.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Created by mhenr on 25.02.2018.
 */
@RestController
public class ContestController {
    private ContestService contestService;

    @Autowired
    public ContestController(final ContestService contestService) {
        this.contestService = contestService;
    }

    @CrossOrigin
    @RequestMapping(value = "contests", method = RequestMethod.GET)
    public Page<ContestResponse> getAllContests(final Pageable pageable) {
        return contestService.getAllContests(pageable);
    }

    @CrossOrigin
    @RequestMapping(value = "contests/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getContestDetails(@PathVariable final Long id) {
        return Response.createResponseEntity(0, contestService.getContestDetails(id), null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "contests", method = RequestMethod.POST)
    public ResponseEntity<?> saveContest(@RequestBody final ContestRequest request) {
        return Response.createResponseEntity(0, contestService.saveContest(request), null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "contests/judges", method = RequestMethod.POST)
    public ResponseEntity<?> addJudgesToContest(@RequestBody final AddJudgeRequest request) {
        contestService.addJudgesToContest(request);
        return Response.createResponseEntity(0, "Judges were added successfully", null, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "contests/{id}/judges", method = RequestMethod.GET)
    public ResponseEntity<?> getJudgesFromContest(@PathVariable final Long id) {
        return Response.createResponseEntity(0, contestService.getJudgesFromContest(id), null, HttpStatus.OK);
    }

    /* ----------------------------------------exception handlers------------------------------------------ */

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<?> unauthorizedUser(UnauthorizedUserException e) {
        return Response.createResponseEntity(1, e.getMessage().isEmpty() ? "Forbidden" : e.getMessage(), null, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> notFound(ObjectNotFoundException e) {
        return Response.createResponseEntity(1, e.getMessage().isEmpty() ? "Not found" : e.getMessage(), null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WrongDataException.class)
    public ResponseEntity<?> wrongData(WrongDataException e) {
        return Response.createResponseEntity(1, e.getMessage().isEmpty() ? "Wrong data" : e.getMessage(), null, HttpStatus.BAD_REQUEST);
    }
}
