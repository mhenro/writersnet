package com.writersnets.services;

import com.writersnets.models.entities.*;
import com.writersnets.models.exceptions.ObjectNotFoundException;
import com.writersnets.models.exceptions.UnauthorizedUserException;
import com.writersnets.models.exceptions.WrongDataException;
import com.writersnets.models.request.AddJudgeRequest;
import com.writersnets.models.request.ContestRequest;
import com.writersnets.models.response.ContestUserResponse;
import com.writersnets.models.response.ContestResponse;
import com.writersnets.repositories.*;
import com.writersnets.utils.ObjectHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mhenr on 25.02.2018.
 */
@Service
@Transactional(readOnly = true)
public class ContestService {
    private ContestRepository contestRepository;
    private ContestJudgeRepository contestJudgeRepository;
    private ContestParticipantRepository contestParticipantRepository;
    private AuthorRepository authorRepository;
    private AuthorizedUserService authorizedUserService;
    private BookRepository bookRepository;
    private NewsService newsService;

    @Autowired
    public ContestService(final ContestRepository contestRepository, final ContestJudgeRepository contestJudgeRepository,
                          final ContestParticipantRepository contestParticipantRepository, final AuthorRepository authorRepository,
                          final AuthorizedUserService authorizedUserService, final BookRepository bookRepository,
                          final NewsService newsService) {
        this.contestRepository = contestRepository;
        this.contestJudgeRepository = contestJudgeRepository;
        this.contestParticipantRepository = contestParticipantRepository;
        this.authorRepository = authorRepository;
        this.authorizedUserService = authorizedUserService;
        this.bookRepository = bookRepository;
        this.newsService = newsService;
    }

    public Page<ContestResponse> getAllContests(final Pageable pageable) {
        return contestRepository.getAllContests(pageable);
    }

    public Page<ContestResponse> getParticipantContests(final String userId, final Pageable pageable) {
        return contestRepository.getParticipantContests(userId, pageable);
    }

    public Page<ContestResponse> getJudgeContests(final String userId, final Pageable pageable) {
        return contestRepository.getJudgeContests(userId, pageable);
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
    public void addParticipantsToContest(final AddJudgeRequest request) {
        final Contest contest = getContest(request.getContestId());
        contestParticipantRepository.clearParticipantsInContest(request.getContestId());
        if (!request.getJudges().isEmpty()) {
            final List<Long> ids = Arrays.stream(request.getJudges().split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            ids.stream().forEach(id -> addParticipantToContest(id, contest));
        }
        updateParticipantCountInContest(request.getContestId());
    }

    @Transactional
    public void removeParticipantFromContest(final Long contestId, final Long bookId) {
        final Contest contest = getContest(contestId);
        if (contest.getClosed()) {
            throw new WrongDataException("You cannot remove an author from the closed contest");
        }
        final Book book = bookRepository.findById(bookId).orElseThrow(() -> new ObjectNotFoundException("Book is not found"));
        final ContestParticipantPK pk = new ContestParticipantPK();
        pk.setContest(contest);
        pk.setParticipant(book.getAuthor());
        pk.setBook(book);
        contestParticipantRepository.deleteById(pk);
        updateParticipantCountInContest(contestId);
    }

    public List<String> getParticipantsIdFromContest(final Long contestId) {
        return contestParticipantRepository.getParticipantBookIdFromContest(contestId);
    }

    public Page<ContestUserResponse> getParticipantsFromContest(final Long contestId, final Pageable pageable) {
        return contestParticipantRepository.getParticipantsFromContest(contestId, pageable);
    }

    public long getNotAcceptedContestCount(final String userId) {
        long asParticipant = contestParticipantRepository.getNotAcceptedContestsByUser(userId);
        long asJudge = contestJudgeRepository.getNotAcceptedContestsByUser(userId);
        long result = asParticipant + asJudge;

        return result;
    }

    public long getParticipantCountFromContest(final Long id) {
        return contestParticipantRepository.getParticipantCountFromContest(id);
    }

    public long getJudgeCountFromContest(final Long id) {
        return contestJudgeRepository.getJudgeCountFromContest(id);
    }

    public boolean isContestReadyToStart(final Long id) {
        final long participants = contestParticipantRepository.getParticipantCountFromContest(id);
        final long judges = contestJudgeRepository.getJudgeCountFromContest(id);
        final long notAcceptedParticipants = contestParticipantRepository.getNotAcceptedParticipantsFromContest(id);
        final long notAcceptedJudges = contestJudgeRepository.getNotAcceptedJudgesFromContest(id);

        return participants > 0 && judges > 0 && notAcceptedJudges == 0 && notAcceptedParticipants == 0;
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
    public void joinInContestAsParticipant(final Long contestId, final Long bookId) {
        final Contest contest = getContest(contestId);
        if (contest.getStarted()) {
            throw new WrongDataException("Contest is already started");
        }
        if (contest.getClosed()) {
            throw new WrongDataException("Contest is already closed");
        }
        bookRepository.findById(bookId).orElseThrow(() -> new WrongDataException("Book is not found"));
        final String authorId = authorizedUserService.getAuthorizedUser().getUsername();
        contestParticipantRepository.joinInContest(authorId, contestId, bookId);
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

    @Transactional
    public void refuseContestAsParticipant(final Long contestId, final Long bookId) {
        final Contest contest = getContest(contestId);
        if (contest.getClosed()) {
            throw new WrongDataException("Contest is already closed");
        }
        bookRepository.findById(bookId).orElseThrow(() -> new WrongDataException("Book is not found"));
        final String authorId = authorizedUserService.getAuthorizedUser().getUsername();
        contestParticipantRepository.refuseContest(authorId, contestId, bookId);
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

    private void addParticipantToContest(final Long bookId, final Contest contest) {
        final Book book = bookRepository.findById(bookId).orElseThrow(() -> new ObjectNotFoundException("Book is not found"));
        final User judge = contestJudgeRepository.getJudgeById(book.getAuthor().getUsername(), contest.getId());
        if (judge != null) {
            throw new WrongDataException("Author cannot be a judge and a participant at the same time in the contest");
        }
        if (contest.getClosed() || contest.getStarted()) {
            throw new WrongDataException("You cannot add an author to the closed/started contest");
        }
        final ContestParticipant participant = new ContestParticipant();
        final ContestParticipantPK pk = new ContestParticipantPK();
        pk.setContest(contest);
        pk.setParticipant(book.getAuthor());
        pk.setBook(book);
        participant.setPk(pk);
        participant.setAccepted(false);
        contestParticipantRepository.save(participant);
    }

    private void updateParticipantCountInContest(final Long id) {
        final Contest contest = getContest(id);
        final long participants = contestParticipantRepository.getParticipantCountFromContest(id);
        contest.setParticipantCount((int)participants);
        contestRepository.save(contest);
    }

    private void updateJudgeCountInContest(final Long id) {
        final Contest contest = getContest(id);
        final long judges = contestJudgeRepository.getJudgeCountFromContest(id);
        contest.setJudgeCount((int)judges);
        contestRepository.save(contest);
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

        newsService.createNews(NewsService.NEWS_TYPE.CONTEST_CREATED, creator, contest);

        return contest.getId();
    }

    private void checkRequest(final ContestRequest request) {
        if (request.getFirstPlaceRevenue().intValue() + request.getSecondPlaceRevenue().intValue() + request.getThirdPlaceRevenue().intValue() != 100) {
            throw new WrongDataException("Total sum of the revenue for 3 places should be equal to 100 percents");
        }
    }

    private User getCreator(final String id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Creator is not found"));
    }

    private Contest getContest(final Long id) {
        return contestRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Contest is not found"));
    }
}
