package com.writersnets.services.contests;

import com.writersnets.models.entities.books.Book;
import com.writersnets.models.entities.contests.Contest;
import com.writersnets.models.entities.contests.ContestParticipant;
import com.writersnets.models.entities.contests.ContestParticipantPK;
import com.writersnets.models.entities.users.User;
import com.writersnets.models.exceptions.ObjectNotFoundException;
import com.writersnets.models.exceptions.WrongDataException;
import com.writersnets.models.request.AddJudgeRequest;
import com.writersnets.models.response.ContestResponse;
import com.writersnets.models.response.ContestUserResponse;
import com.writersnets.repositories.*;
import com.writersnets.services.authors.NewsService;
import com.writersnets.services.security.AuthorizedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ParticipantContestService {
    private ContestRepository contestRepository;
    private ContestJudgeRepository contestJudgeRepository;
    private ContestParticipantRepository contestParticipantRepository;
    private AuthorizedUserService authorizedUserService;
    private BookRepository bookRepository;
    private NewsService newsService;

    @Autowired
    public ParticipantContestService(final ContestRepository contestRepository, final ContestJudgeRepository contestJudgeRepository,
                                     final ContestParticipantRepository contestParticipantRepository, final AuthorizedUserService authorizedUserService,
                                     final BookRepository bookRepository, final NewsService newsService) {
        this.contestRepository = contestRepository;
        this.contestJudgeRepository = contestJudgeRepository;
        this.contestParticipantRepository = contestParticipantRepository;
        this.authorizedUserService = authorizedUserService;
        this.bookRepository = bookRepository;
        this.newsService = newsService;
    }

    public Page<ContestResponse> getParticipantContests(final String userId, final Pageable pageable) {
        return contestRepository.getParticipantContests(userId, pageable);
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

    public long getParticipantCountFromContest(final Long id) {
        return contestParticipantRepository.getParticipantCountFromContest(id);
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
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new WrongDataException("Book is not found"));
        final String authorId = authorizedUserService.getAuthorizedUser().getUsername();
        contestParticipantRepository.joinInContest(authorId, contestId, bookId);

        newsService.createNews(NewsService.NEWS_TYPE.JOIN_IN_CONTEST_AS_PARTICIPANT, authorizedUserService.getAuthorizedUser(), contest, book);
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

    private void addParticipantToContest(final Long bookId, final Contest contest) {
        final Book book = bookRepository.findById(bookId).orElseThrow(() -> new ObjectNotFoundException("Book is not found"));
        final Optional<User> judge = contestJudgeRepository.getJudgeById(book.getAuthor().getUsername(), contest.getId());
        if (judge.isPresent()) {
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
        contest.setParticipantCount((int) participants);
        contestRepository.save(contest);
    }

    private Contest getContest(final Long id) {
        return contestRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Contest is not found"));
    }
}
