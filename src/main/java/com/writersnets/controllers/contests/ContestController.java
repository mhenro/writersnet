package com.writersnets.controllers.contests;

import com.writersnets.models.Response;
import com.writersnets.models.request.ContestRequest;
import com.writersnets.models.response.ContestRatingDetailsResponse;
import com.writersnets.models.response.ContestRatingResponse;
import com.writersnets.models.response.ContestResponse;
import com.writersnets.services.contests.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Created by mhenr on 25.02.2018.
 */
@CrossOrigin
@RestController
@RequestMapping("contests")
public class ContestController {
    private ContestService contestService;

    @Autowired
    public ContestController(final ContestService contestService) {
        this.contestService = contestService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<ContestResponse> getAllContests(final Pageable pageable) {
        return contestService.getAllContests(pageable);
    }

    @RequestMapping(value = "/creators/{creatorId}", method = RequestMethod.GET)
    public Page<ContestResponse> getCreatorContests(@PathVariable final String creatorId, final Pageable pageable) {
        return contestService.getCreatorContests(creatorId, pageable);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getContestDetails(@PathVariable final Long id) {
        return Response.createResponseEntity(0, contestService.getContestDetails(id), null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> saveContest(@RequestBody final ContestRequest request) {
        return Response.createResponseEntity(0, contestService.saveContest(request), null, HttpStatus.OK);
    }

    @RequestMapping(value = "/not-accepted/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getNotAcceptedContestCount(@PathVariable final String userId) {
        return Response.createResponseEntity(0, contestService.getNotAcceptedContestCount(userId), null, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/readiness", method = RequestMethod.GET)
    public ResponseEntity<?> isContestReadyToStart(@PathVariable final Long id) {
        return Response.createResponseEntity(0, contestService.isContestReadyToStart(id), null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{contestId}/start", method = RequestMethod.GET)
    public ResponseEntity<?> startContest(@PathVariable final Long contestId) {
        contestService.startContest(contestId);
        return Response.createResponseEntity(0, "Contest was started successfully", null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{contestId}/finish", method = RequestMethod.GET)
    public ResponseEntity<?> finishContest(@PathVariable final Long contestId, final Authentication auth) {
        contestService.finishContest(contestId, auth);
        return Response.createResponseEntity(0, "Contest was finished successfully", null, HttpStatus.OK);
    }

    @RequestMapping(value = "/{contestId}/ratings", method = RequestMethod.GET)
    public Page<ContestRatingResponse> getParticipantsRating(@PathVariable final long contestId, final Pageable pageable) {
        return contestService.getParticipantsRating(contestId, pageable);
    }

    @RequestMapping(value = "/{contestId}/ratings/{bookId}", method = RequestMethod.GET)
    public Page<ContestRatingDetailsResponse> getParticipantsRatingDetails(@PathVariable final long contestId, @PathVariable final long bookId, final Pageable pageable) {
        return contestService.getParticipantsRatingDetails(contestId, bookId, pageable);
    }
}
