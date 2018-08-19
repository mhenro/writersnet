package com.writersnets.controllers.contests;

import com.writersnets.models.Response;
import com.writersnets.models.request.AddJudgeRequest;
import com.writersnets.models.response.ContestResponse;
import com.writersnets.services.contests.JudgeContestService;
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
public class JudgeContestController {
    private JudgeContestService contestService;

    @Autowired
    public JudgeContestController(final JudgeContestService contestService) {
        this.contestService = contestService;
    }

    @RequestMapping(value = "/judges/{userId}", method = RequestMethod.GET)
    public Page<ContestResponse> getJudgeContests(@PathVariable final String userId, final Pageable pageable) {
        return contestService.getJudgeContests(userId, pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/judges", method = RequestMethod.POST)
    public ResponseEntity<?> addJudgesIdToContest(@RequestBody final AddJudgeRequest request) {
        contestService.addJudgesToContest(request);
        return Response.createResponseEntity(0, "Judges were added successfully", null, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/judges", method = RequestMethod.GET)
    public ResponseEntity<?> getJudgesIdFromContest(@PathVariable final Long id) {
        return Response.createResponseEntity(0, contestService.getJudgesIdFromContest(id), null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{contestId}/judges/{judgeId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeJudgeFromContest(@PathVariable final Long contestId, @PathVariable final String judgeId) {
        contestService.removeJudgeFromContest(contestId, judgeId);
        return Response.createResponseEntity(0, "Judge was removed successfully from contest", null, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/judges-full", method = RequestMethod.GET)
    public ResponseEntity<?> getJudgesFromContest(@PathVariable final Long id, final Pageable pageable) {
        return Response.createResponseEntity(0, contestService.getJudgesFromContest(id, pageable), null, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/judges-count", method = RequestMethod.GET)
    public ResponseEntity<?> getJudgeCountFromContest(@PathVariable final Long id) {
        return Response.createResponseEntity(0, contestService.getJudgeCountFromContest(id), null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{contestId}/join", method = RequestMethod.GET)
    public ResponseEntity<?> joinInContestAsJudge(@PathVariable final Long contestId) {
        contestService.joinInContestAsJudge(contestId);
        return Response.createResponseEntity(0, "You are successfully joined to the contest", null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{contestId}/refuse", method = RequestMethod.GET)
    public ResponseEntity<?> refuseContestAsJudge(@PathVariable final Long contestId) {
        contestService.refuseContestAsJudge(contestId);
        return Response.createResponseEntity(0, "You successfully refused the contest", null, HttpStatus.OK);
    }
}
