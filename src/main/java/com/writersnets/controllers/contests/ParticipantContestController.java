package com.writersnets.controllers.contests;

import com.writersnets.models.Response;
import com.writersnets.models.request.AddJudgeRequest;
import com.writersnets.models.response.ContestResponse;
import com.writersnets.services.contests.ParticipantContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("contests")
public class ParticipantContestController {
    private ParticipantContestService contestService;

    @Autowired
    public ParticipantContestController(final ParticipantContestService contestService) {
        this.contestService = contestService;
    }

    @RequestMapping(value = "/participants/{userId}", method = RequestMethod.GET)
    public Page<ContestResponse> getParticipantContests(@PathVariable final String userId, final Pageable pageable) {
        return contestService.getParticipantContests(userId, pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/participants", method = RequestMethod.POST)
    public ResponseEntity<?> addParticipantsToContest(@RequestBody final AddJudgeRequest request) {
        contestService.addParticipantsToContest(request);
        return Response.createResponseEntity(0, "Participants were added successfully", null, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipantsIdFromContest(@PathVariable final Long id) {
        return Response.createResponseEntity(0, contestService.getParticipantsIdFromContest(id), null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{contestId}/participants/{bookId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeParticipantFromContest(@PathVariable final Long contestId, @PathVariable final Long bookId) {
        contestService.removeParticipantFromContest(contestId, bookId);
        return Response.createResponseEntity(0, "Participant was removed successfully from contest", null, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants-full", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipantsFromContest(@PathVariable final Long id, final Pageable pageable) {
        return Response.createResponseEntity(0, contestService.getParticipantsFromContest(id, pageable), null, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants-count", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipantCountFromContest(@PathVariable final Long id) {
        return Response.createResponseEntity(0, contestService.getParticipantCountFromContest(id), null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{contestId}/join/{bookId}", method = RequestMethod.GET)
    public ResponseEntity<?> joinInContestAsParticipant(@PathVariable final Long contestId, @PathVariable final Long bookId) {
        contestService.joinInContestAsParticipant(contestId, bookId);
        return Response.createResponseEntity(0, "You are successfully joined to the contest", null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{contestId}/refuse/{bookId}", method = RequestMethod.GET)
    public ResponseEntity<?> refuseContestAsParticipant(@PathVariable final Long contestId, @PathVariable final Long bookId) {
        contestService.refuseContestAsParticipant(contestId, bookId);
        return Response.createResponseEntity(0, "You successfully refused the contest", null, HttpStatus.OK);
    }
}
