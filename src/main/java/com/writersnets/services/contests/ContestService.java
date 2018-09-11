package com.writersnets.services.contests;

import com.writersnets.models.OperationType;
import com.writersnets.models.entities.books.Book;
import com.writersnets.models.entities.contests.Contest;
import com.writersnets.models.entities.users.User;
import com.writersnets.models.exceptions.ObjectNotFoundException;
import com.writersnets.models.exceptions.UnauthorizedUserException;
import com.writersnets.models.exceptions.WrongDataException;
import com.writersnets.models.request.BuyRequest;
import com.writersnets.models.request.ContestRequest;
import com.writersnets.models.response.ContestRatingDetailsResponse;
import com.writersnets.models.response.ContestRatingResponse;
import com.writersnets.models.response.ContestResponse;
import com.writersnets.models.security.AuthUser;
import com.writersnets.repositories.*;
import com.writersnets.services.authors.BalanceService;
import com.writersnets.services.security.AuthorizedUserService;
import com.writersnets.services.authors.NewsService;
import com.writersnets.utils.ObjectHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Created by mhenr on 25.02.2018.
 */
@Service
@Transactional(readOnly = true)
public class ContestService {
    private ContestRepository contestRepository;
    private ContestJudgeRepository contestJudgeRepository;
    private ContestParticipantRepository contestParticipantRepository;
    private ContestRatingRepository contestRatingRepository;
    private AuthorizedUserService authorizedUserService;
    private NewsService newsService;
    private BalanceService balanceService;
    private AuthorRepository authorRepository;
    private BookRepository bookRepository;

    @Autowired
    public ContestService(final ContestRepository contestRepository, final ContestJudgeRepository contestJudgeRepository,
                          final ContestParticipantRepository contestParticipantRepository, final ContestRatingRepository contestRatingRepository,
                          final AuthorizedUserService authorizedUserService, final NewsService newsService,
                          final BalanceService balanceService, final AuthorRepository authorRepository,
                          final BookRepository bookRepository) {
        this.contestRepository = contestRepository;
        this.contestJudgeRepository = contestJudgeRepository;
        this.contestParticipantRepository = contestParticipantRepository;
        this.contestRatingRepository = contestRatingRepository;
        this.authorizedUserService = authorizedUserService;
        this.newsService = newsService;
        this.balanceService = balanceService;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public Page<ContestResponse> getAllContests(final Pageable pageable) {
        return contestRepository.getAllContests(pageable);
    }

    public Page<ContestResponse> getCreatorContests(final String creatorId, final Pageable pageable) {
        return contestRepository.getCreatorContests(creatorId, pageable);
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

    public long getNotAcceptedContestCount(final String userId) {
        long asParticipant = contestParticipantRepository.getNotAcceptedContestsByUser(userId);
        long asJudge = contestJudgeRepository.getNotAcceptedContestsByUser(userId);
        long result = asParticipant + asJudge;

        return result;
    }

    public boolean isContestReadyToStart(final Long id) {
        final long participants = contestParticipantRepository.getParticipantCountFromContest(id);
        final long judges = contestJudgeRepository.getJudgeCountFromContest(id);
        final long notAcceptedParticipants = contestParticipantRepository.getNotAcceptedParticipantsFromContest(id);
        final long notAcceptedJudges = contestJudgeRepository.getNotAcceptedJudgesFromContest(id);

        return participants > 0 && judges > 0 && notAcceptedJudges == 0 && notAcceptedParticipants == 0;
    }

    @Transactional
    public void startContest(final Long id) {
        final Contest contest = getContest(id);
        if (!isContestReadyToStart(contest.getId())) {
            throw new WrongDataException("Contest is not ready for starting");
        }
        if (contest.getClosed() || contest.getStarted()) {
            throw new WrongDataException("Contest is already closed/started");
        }
        contest.setStarted(true);
        contestRepository.save(contest);
    }

    @Transactional
    public void finishContest(final long contestId, final Authentication auth) {
        final String userName = ((AuthUser) auth.getPrincipal()).getUserDetails().getUsername();
        final Contest contest = getContest(contestId);
        if (!contest.getCreator().getUsername().equals(userName)) {
            throw new WrongDataException("Only creator can finish the contest");
        }
        finishContest(contest);
    }

    public Page<ContestRatingResponse> getParticipantsRating(final long contestId, final Pageable pageable) {
        return contestRatingRepository.getParticipantsRating(contestId, pageable);
    }

    public Page<ContestRatingDetailsResponse> getParticipantsRatingDetails(final long contestId, final long bookId, final Pageable pageable) {
        return contestRatingRepository.getParticipantsRatingDetails(contestId, bookId, pageable);
    }

    /* search contests which limit is expired and finish them forcibly if it possible */
    @Transactional
    @Scheduled(fixedDelay = 18000000) //5 hours
    public void finishContestForcibly() {
        PageRequest request = PageRequest.of(0, 50);
        Page<Contest> contests = contestRepository.getExpiredContests(LocalDateTime.now(), request);
        contests.getContent().forEach(contest -> finishContest(contest));
    }

    private Long editContest(final ContestRequest request) {
        final User creator = authorizedUserService.getAuthorizedUser();
        final Contest contest = getContest(request.getId());
        if (!contest.getCreator().getUsername().equals(creator.getUsername())) {
            throw new UnauthorizedUserException();
        }
        if (contest.getClosed() || contest.getStarted()) {
            throw new WrongDataException("Contest is already closed/started");
        }
        BeanUtils.copyProperties(request, contest, ObjectHelper.getNullPropertyNames(request));
        contest.setCreator(creator);
        contest.setCreated(LocalDateTime.now());
        contestRepository.save(contest);
        return contest.getId();
    }

    private Long createContest(final ContestRequest request) {
        final User creator = authorizedUserService.getAuthorizedUser();
        final Contest contest = new Contest();
        BeanUtils.copyProperties(request, contest, ObjectHelper.getNullPropertyNames(request));
        contest.setCreator(creator);
        contest.setCreated(LocalDateTime.now());
        contest.setStarted(false);
        contest.setClosed(false);
        contest.setJudgeCount(0);
        contest.setParticipantCount(0);
        contestRepository.save(contest);

        newsService.createNews(NewsService.NEWS_TYPE.CONTEST_CREATED, creator, contest, null);

        return contest.getId();
    }

    private void checkRequest(final ContestRequest request) {
        if (request.getFirstPlaceRevenue().intValue() + request.getSecondPlaceRevenue().intValue() + request.getThirdPlaceRevenue().intValue() != 100) {
            throw new WrongDataException("Total sum of the revenue for 3 places should be equal to 100 percents");
        }
    }

    private Contest getContest(final Long id) {
        return contestRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Contest is not found"));
    }

    public void finishContest(final Contest contest) {
        if (!contest.getStarted()) {
            throw new WrongDataException("Contest is not started yet");
        }
        if (contest.getClosed()) {
            throw new WrongDataException("Contest is already finished");
        }
        PageRequest pageable = PageRequest.of(0, 3);
        Page<ContestRatingResponse> results = contestRatingRepository.getParticipantsRating(contest.getId(), pageable);
        BuyRequest request = new BuyRequest();
        request.setOperationType(OperationType.CONTEST_AWARD);
        request.setPurchaseId(contest.getId());
        long firstRevenueAmount = contest.getFirstRevenueAmount();
        long secondRevenueAmount = contest.getSecondRevenueAmount();
        long thirdRevenueAmount = contest.getThirdRevenueAmount();
        if (results.getTotalElements() > 0 && contest.getFirstPlaceRevenue() > 0) {  //return revenue for first place to the participant
            request.setSourceUserId(results.getContent().get(0).getParticipantId());
            request.setAmount(firstRevenueAmount);
            balanceService.processOperation(request);

            Book winnerBook = bookRepository.findById(results.getContent().get(0).getBookId()).orElseGet(() -> null);
            authorRepository.findById(results.getContent().get(0).getParticipantId())
                    .ifPresent(winner -> newsService.createNews(NewsService.NEWS_TYPE.WON_IN_CONTEST, winner, contest, winnerBook));
        }
        if (results.getTotalElements() > 1 && contest.getSecondPlaceRevenue() > 0) { //return revenue for second place to the participant
            request.setSourceUserId(results.getContent().get(1).getParticipantId());
            request.setAmount(secondRevenueAmount);
            balanceService.processOperation(request);
        }
        if (results.getTotalElements() > 2 && contest.getThirdPlaceRevenue() > 0) {  //return revenue for third place to the participant
            request.setSourceUserId(results.getContent().get(2).getParticipantId());
            request.setAmount(thirdRevenueAmount);
            balanceService.processOperation(request);
        }
        /* if the prize fund is not exhausted return all to the contest creator */
        if (results.getTotalElements() == 0 || contest.getPrizeFund() > 0) {
            request.setSourceUserId(contest.getCreator().getUsername());
            request.setAmount(contest.getPrizeFund());
            balanceService.processOperation(request);
        }
        contest.setStarted(false);
        contest.setClosed(true);
    }
}
