package org.booklink.services;

import org.booklink.models.entities.Contest;
import org.booklink.models.entities.ContestJudge;
import org.booklink.models.entities.ContestJudgePK;
import org.booklink.models.entities.User;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.WrongDataException;
import org.booklink.models.request.AddJudgeRequest;
import org.booklink.models.request.ContestRequest;
import org.booklink.models.response.ContestResponse;
import org.booklink.repositories.AuthorRepository;
import org.booklink.repositories.ContestJudgeRepository;
import org.booklink.repositories.ContestRepository;
import org.booklink.utils.ObjectHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mhenr on 25.02.2018.
 */
@Service
@Transactional(readOnly = true)
public class ContestService {
    private ContestRepository contestRepository;
    private ContestJudgeRepository contestJudgeRepository;
    private AuthorRepository authorRepository;

    @Autowired
    public ContestService(final ContestRepository contestRepository, final ContestJudgeRepository contestJudgeRepository,
                          final AuthorRepository authorRepository) {
        this.contestRepository = contestRepository;
        this.contestJudgeRepository = contestJudgeRepository;
        this.authorRepository = authorRepository;
    }

    public Page<ContestResponse> getAllContests(final Pageable pageable) {
        return contestRepository.getAllContests(pageable);
    }

    public ContestResponse getContestDetails(final Long id) {
        final ContestResponse response = contestRepository.getContest(id);
        if (response == null) {
            throw new ObjectNotFoundException("Contest is not found");
        }
        return response;
    }

    @Transactional
    public Long saveContest(final ContestRequest request) {
        checkRequest(request);
        final Long id;
        if (request.getId() != null) {
            id = editContest(request);
        } else {
            id = createContest(request);
        }
        return id;
    }

    @Transactional
    public void addJudgesToContest(final AddJudgeRequest request) {
        final Contest contest = contestRepository.findOne(request.getContestId());
        if (contest == null) {
            throw new ObjectNotFoundException("Contest is not found");
        }
        final List<String> ids = Arrays.asList(request.getJudges().split(","));
        ids.stream().forEach(id -> addJudgeToContest(id, contest));
    }

    private void addJudgeToContest(final String judgeId, final Contest contest) {
        final User author = authorRepository.findOne(judgeId);
        if (author == null) {
            throw new ObjectNotFoundException("Judge is not found");
        }
        final ContestJudge judge = new ContestJudge();
        final ContestJudgePK pk = new ContestJudgePK();
        pk.setContest(contest);
        pk.setJudge(author);
        judge.setPk(pk);
        judge.setAccepted(false);
        contestJudgeRepository.save(judge);
    }

    private Long editContest(final ContestRequest request) {
        final Contest contest = getContest(request.getId());
        final User creator = getCreator(request.getCreatorId());
        BeanUtils.copyProperties(request, contest, ObjectHelper.getNullPropertyNames(request));
        contest.setCreator(creator);
        contest.setCreated(LocalDateTime.now());
        contestRepository.save(contest);
        return contest.getId();
    }

    private Long createContest(final ContestRequest request) {
        final User creator = getCreator(request.getCreatorId());
        final Contest contest = new Contest();
        BeanUtils.copyProperties(request, contest, ObjectHelper.getNullPropertyNames(request));
        contest.setCreator(creator);
        contest.setCreated(LocalDateTime.now());
        contestRepository.save(contest);
        return contest.getId();
    }

    private void checkRequest(final ContestRequest request) {
        if (request.getFirstPlaceRevenue() + request.getSecondPlaceRevenue() + request.getThirdPlaceRevenue() > 100) {
            throw new WrongDataException("Total sum of the revenue for 3 places should not be greater than 100 percents");
        }
    }

    private User getCreator(final String id) {
        final User creator = authorRepository.findOne(id);
        if (creator == null) {
            throw new ObjectNotFoundException("Creator is not found");
        }
        return creator;
    }

    private Contest getContest(final Long id) {
        final Contest contest = contestRepository.findOne(id);
        if (contest == null) {
            throw new ObjectNotFoundException("Contest is not found");
        }
        return contest;
    }
}
