package org.booklink.controllers;

import org.booklink.models.Response;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.exceptions.WrongDataException;
import org.booklink.models.request.AddJudgeRequest;
import org.booklink.models.request.ContestRequest;
import org.booklink.models.response.ContestResponse;
import org.booklink.services.ContestService;
import org.booklink.utils.ControllerHelper;
import org.booklink.utils.ObjectHelper;
import org.bouncycastle.ocsp.Req;
import org.hibernate.StaleStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Created by mhenr on 25.02.2018.
 */
@CrossOrigin
@RestController
public class ContestController {
    private ContestService contestService;

    @Autowired
    public ContestController(final ContestService contestService) {
        this.contestService = contestService;
    }

    @RequestMapping(value = "contests", method = RequestMethod.GET)
    public Page<ContestResponse> getAllContests(final Pageable pageable) {
        return contestService.getAllContests(pageable);
    }

    @RequestMapping(value = "contests/participants/{userId}", method = RequestMethod.GET)
    public Page<ContestResponse> getParticipantContests(@PathVariable final String userId, final Pageable pageable) {
        return contestService.getParticipantContests(userId, pageable);
    }

    @RequestMapping(value = "contests/judges/{userId}", method = RequestMethod.GET)
    public Page<ContestResponse> getJudgeContests(@PathVariable final String userId, final Pageable pageable) {
        return contestService.getJudgeContests(userId, pageable);
    }

    @RequestMapping(value = "contests/creators/{creatorId}", method = RequestMethod.GET)
    public Page<ContestResponse> getCreatorContests(@PathVariable final String creatorId, final Pageable pageable) {
        return contestService.getCreatorContests(creatorId, pageable);
    }

    @RequestMapping(value = "contests/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getContestDetails(@PathVariable final Long id) {
        return Response.createResponseEntity(0, contestService.getContestDetails(id), null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "contests", method = RequestMethod.POST)
    public ResponseEntity<?> saveContest(@RequestBody final ContestRequest request) {
        return Response.createResponseEntity(0, contestService.saveContest(request), null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "contests/judges", method = RequestMethod.POST)
    public ResponseEntity<?> addJudgesIdToContest(@RequestBody final AddJudgeRequest request) {
        contestService.addJudgesToContest(request);
        return Response.createResponseEntity(0, "Judges were added successfully", null, HttpStatus.OK);
    }

    @RequestMapping(value = "contests/{id}/judges", method = RequestMethod.GET)
    public ResponseEntity<?> getJudgesIdFromContest(@PathVariable final Long id) {
        return Response.createResponseEntity(0, contestService.getJudgesIdFromContest(id), null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "contests/{contestId}/judges/{judgeId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeJudgeFromContest(@PathVariable final Long contestId, @PathVariable final String judgeId) {
        contestService.removeJudgeFromContest(contestId, judgeId);
        return Response.createResponseEntity(0, "Judge was removed successfully from contest", null, HttpStatus.OK);
    }

    @RequestMapping(value = "contests/{id}/judges-full", method = RequestMethod.GET)
    public ResponseEntity<?> getJudgesFromContest(@PathVariable final Long id, final Pageable pageable) {
        return Response.createResponseEntity(0, contestService.getJudgesFromContest(id, pageable), null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "contests/participants", method = RequestMethod.POST)
    public ResponseEntity<?> addParticipantsToContest(@RequestBody final AddJudgeRequest request) {
        contestService.addParticipantsToContest(request);
        return Response.createResponseEntity(0, "Participants were added successfully", null, HttpStatus.OK);
    }

    @RequestMapping(value = "contests/{id}/participants", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipantsIdFromContest(@PathVariable final Long id) {
        return Response.createResponseEntity(0, contestService.getParticipantsIdFromContest(id), null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "contests/{contestId}/participants/{bookId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeParticipantFromContest(@PathVariable final Long contestId, @PathVariable final Long bookId) {
        contestService.removeParticipantFromContest(contestId, bookId);
        return Response.createResponseEntity(0, "Participant was removed successfully from contest", null, HttpStatus.OK);
    }

    @RequestMapping(value = "contests/{id}/participants-full", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipantsFromContest(@PathVariable final Long id, final Pageable pageable) {
        return Response.createResponseEntity(0, contestService.getParticipantsFromContest(id, pageable), null, HttpStatus.OK);
    }

    @RequestMapping(value = "contests/not-accepted/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getNotAcceptedContestCount(@PathVariable final String userId) {
        return Response.createResponseEntity(0, contestService.getNotAcceptedContestCount(userId), null, HttpStatus.OK);
    }

    @RequestMapping(value = "contests/{id}/participants-count", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipantCountFromContest(@PathVariable final Long id) {
        return Response.createResponseEntity(0, contestService.getParticipantCountFromContest(id), null, HttpStatus.OK);
    }

    @RequestMapping(value = "contests/{id}/judges-count", method = RequestMethod.GET)
    public ResponseEntity<?> getJudgeCountFromContest(@PathVariable final Long id) {
        return Response.createResponseEntity(0, contestService.getJudgeCountFromContest(id), null, HttpStatus.OK);
    }

    @RequestMapping(value = "contests/{id}/readiness", method = RequestMethod.GET)
    public ResponseEntity<?> isContestReadyToStart(@PathVariable final Long id) {
        return Response.createResponseEntity(0, contestService.isContestReadyToStart(id), null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "contests/{contestId}/join", method = RequestMethod.GET)
    public ResponseEntity<?> joinInContestAsJudge(@PathVariable final Long contestId) {
        contestService.joinInContestAsJudge(contestId);
        return Response.createResponseEntity(0, "You are successfully joined to the contest", null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "contests/{contestId}/join/{bookId}", method = RequestMethod.GET)
    public ResponseEntity<?> joinInContestAsParticipant(@PathVariable final Long contestId, @PathVariable final Long bookId) {
        contestService.joinInContestAsParticipant(contestId, bookId);
        return Response.createResponseEntity(0, "You are successfully joined to the contest", null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "contests/{contestId}/refuse", method = RequestMethod.GET)
    public ResponseEntity<?> refuseContestAsJudge(@PathVariable final Long contestId) {
        contestService.refuseContestAsJudge(contestId);
        return Response.createResponseEntity(0, "You successfully refused the contest", null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "contests/{contestId}/refuse/{bookId}", method = RequestMethod.GET)
    public ResponseEntity<?> refuseContestAsParticipant(@PathVariable final Long contestId, @PathVariable final Long bookId) {
        contestService.refuseContestAsParticipant(contestId, bookId);
        return Response.createResponseEntity(0, "You successfully refused the contest", null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "contests/{contestId}/start", method = RequestMethod.GET)
    public ResponseEntity<?> startContest(@PathVariable final Long contestId) {
        contestService.startContest(contestId);
        return Response.createResponseEntity(0, "Contest was started successfully", null, HttpStatus.OK);
    }

    /* ----------------------------------------exception handlers------------------------------------------ */

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<?> unauthorizedUser(UnauthorizedUserException e) {
        return Response.createResponseEntity(1, ControllerHelper.getErrorOrDefaultMessage(e, "Forbidden"), null, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> notFound(ObjectNotFoundException e) {
        return Response.createResponseEntity(1, ControllerHelper.getErrorOrDefaultMessage(e, "Not found"), null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WrongDataException.class)
    public ResponseEntity<?> wrongData(WrongDataException e) {
        return Response.createResponseEntity(1, ControllerHelper.getErrorOrDefaultMessage(e, "Wrong data"), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StaleStateException.class)
    public ResponseEntity<?> optimisticLockCollision(StaleStateException e) {
        return Response.createResponseEntity(1, "Server is overloaded. Please repeat the operation.", null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
