package com.writersnets.services.contests;

import com.writersnets.models.entities.contests.Contest;
import com.writersnets.models.entities.contests.ContestJudge;
import com.writersnets.models.entities.contests.ContestJudgePK;
import com.writersnets.models.entities.users.User;
import com.writersnets.models.exceptions.ObjectNotFoundException;
import com.writersnets.models.exceptions.WrongDataException;
import com.writersnets.models.request.AddJudgeRequest;
import com.writersnets.models.response.ContestResponse;
import com.writersnets.models.response.ContestUserResponse;
import com.writersnets.repositories.*;
import com.writersnets.services.AuthorizedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class JudgeContestService {
    private ContestRepository contestRepository;
    private ContestJudgeRepository contestJudgeRepository;
    private ContestParticipantRepository contestParticipantRepository;
    private AuthorRepository authorRepository;
    private AuthorizedUserService authorizedUserService;

    @Autowired
    public JudgeContestService(final ContestRepository contestRepository, final ContestJudgeRepository contestJudgeRepository,
                               final ContestParticipantRepository contestParticipantRepository, final AuthorRepository authorRepository,
                               final AuthorizedUserService authorizedUserService) {
        this.contestRepository = contestRepository;
        this.contestJudgeRepository = contestJudgeRepository;
        this.contestParticipantRepository = contestParticipantRepository;
        this.authorRepository = authorRepository;
        this.authorizedUserService = authorizedUserService;
    }

    public Page<ContestResponse> getJudgeContests(final String userId, final Pageable pageable) {
        return contestRepository.getJudgeContests(userId, pageable);
    }

    @Transactional
    public void addJudgesToContest(final AddJudgeRequest request) {
        final Contest contest = getContest(request.getContestId());
        contestJudgeRepository.clearJudgesInContest(request.getContestId());
        if (!request.getJudges().isEmpty()) {
            final List<String> ids = Arrays.asList(request.getJudges().split(","));
            ids.stream().forEach(id -> addJudgeToContest(id, contest));
        }

        updateJudgeCountInContest(request.getContestId());
    }

    @Transactional
    public void removeJudgeFromContest(final Long contestId, final String judgeId) {
        final Contest contest = getContest(contestId);
        if (contest.getClosed()) {
            throw new WrongDataException("You cannot remove an author from the closed contest");
        }
        final User judge = authorRepository.findById(judgeId).orElseThrow(() -> new ObjectNotFoundException("Judge is not found"));
        final ContestJudgePK pk = new ContestJudgePK();
        pk.setContest(contest);
        pk.setJudge(judge);
        contestJudgeRepository.deleteById(pk);
        updateJudgeCountInContest(contestId);
    }

    public List<String> getJudgesIdFromContest(final Long contestId) {
        return contestJudgeRepository.getJudgesIdFromContest(contestId);
    }

    public Page<ContestUserResponse> getJudgesFromContest(final Long contestId, final Pageable pageable) {
        return contestJudgeRepository.getJudgesFromContest(contestId, pageable);
    }

    @Transactional
    public void joinInContestAsJudge(final Long contestId) {
        final Contest contest = getContest(contestId);
        if (contest.getStarted()) {
            throw new WrongDataException("Contest is already started");
        }
        if (contest.getClosed()) {
            throw new WrongDataException("Contest is already closed");
        }
        final String authorId = authorizedUserService.getAuthorizedUser().getUsername();
        contestJudgeRepository.joinInContest(authorId, contestId);
    }

    @Transactional
    public void refuseContestAsJudge(final Long contestId) {
        final Contest contest = getContest(contestId);
        if (contest.getClosed()) {
            throw new WrongDataException("Contest is already closed");
        }
        final String authorId = authorizedUserService.getAuthorizedUser().getUsername();
        contestJudgeRepository.refuseContest(authorId, contestId);
    }

    public long getJudgeCountFromContest(final Long id) {
        return contestJudgeRepository.getJudgeCountFromContest(id);
    }

    private void addJudgeToContest(final String judgeId, final Contest contest) {
        final User author = authorRepository.findById(judgeId).orElseThrow(() -> new ObjectNotFoundException("Judge is not found"));
        final User participant = contestParticipantRepository.getParticipantById(judgeId, contest.getId());
        if (participant != null) {
            throw new WrongDataException("Author cannot be a judge and a participant at the same time in the contest");
        }
        if (contest.getClosed() || contest.getStarted()) {
            throw new WrongDataException("You cannot add an author to the closed/started contest");
        }
        final ContestJudge judge = new ContestJudge();
        final ContestJudgePK pk = new ContestJudgePK();
        pk.setContest(contest);
        pk.setJudge(author);
        judge.setPk(pk);
        judge.setAccepted(false);
        contestJudgeRepository.save(judge);
    }

    private void updateJudgeCountInContest(final Long id) {
        final Contest contest = getContest(id);
        final long judges = contestJudgeRepository.getJudgeCountFromContest(id);
        contest.setJudgeCount((int) judges);
        contestRepository.save(contest);
    }

    private Contest getContest(final Long id) {
        return contestRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Contest is not found"));
    }
}
