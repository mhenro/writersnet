package org.booklink.services;

import org.booklink.models.entities.*;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.exceptions.WrongDataException;
import org.booklink.models.request.AddJudgeRequest;
import org.booklink.models.request.ContestRequest;
import org.booklink.models.response.ContestUserResponse;
import org.booklink.models.response.ContestResponse;
import org.booklink.repositories.*;
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
        final User judge = authorRepository.findOne(judgeId);
        if (judge == null) {
            throw new ObjectNotFoundException("Judge is not found");
        }
        final ContestJudgePK pk = new ContestJudgePK();
        pk.setContest(contest);
        pk.setJudge(judge);
        contestJudgeRepository.delete(pk);
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
        final Book book = bookRepository.findOne(bookId);
        if (book == null) {
            throw new ObjectNotFoundException("Book is not found");
        }
        final ContestParticipantPK pk = new ContestParticipantPK();
        pk.setContest(contest);
        pk.setParticipant(book.getAuthor());
        pk.setBook(book);
        contestParticipantRepository.delete(pk);
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
    public void joinInContest(final Long contestId) {
        final Contest contest = getContest(contestId);
        if (contest.getStarted()) {
            throw new WrongDataException("Contest is already started");
        }
        if (contest.getClosed()) {
            throw new WrongDataException("Contest is already closed");
        }
        final String authorId = authorizedUserService.getAuthorizedUser().getUsername();
        final User participant = contestParticipantRepository.getParticipantById(authorId, contestId);
        if (participant != null) {
            contestParticipantRepository.joinInContest(authorId, contestId);
            return;
        }
        final User judge = contestJudgeRepository.getJudgeById(authorId, contestId);
        if (judge != null) {
            contestJudgeRepository.joinInContest(authorId, contestId);
            return;
        }
        throw new ObjectNotFoundException("User is not found");
    }

    @Transactional
    public void startContest(final Long id) {
        final Contest contest = getContest(id);
        if (!isContestReadyToStart(contest.getId())) {
            throw new WrongDataException("Contest is not ready for starting");
        }
        if (contest.getClosed()) {
            throw new WrongDataException("Contest is already closed");
        }
        contest.setStarted(true);
        contestRepository.save(contest);
    }

    private void addJudgeToContest(final String judgeId, final Contest contest) {
        final User author = authorRepository.findOne(judgeId);
        if (author == null) {
            throw new ObjectNotFoundException("Judge is not found");
        }
        final User participant = contestParticipantRepository.getParticipantById(judgeId, contest.getId());
        if (participant != null) {
            throw new WrongDataException("Author cannot be a judge and a participant at the same time in the contest");
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
        final Book book = bookRepository.findOne(bookId);
        if (book == null) {
            throw new ObjectNotFoundException("Book is not found");
        }
        final User judge = contestJudgeRepository.getJudgeById(book.getAuthor().getUsername(), contest.getId());
        if (judge != null) {
            throw new WrongDataException("Author cannot be a judge and a participant at the same time in the contest");
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
