package com.writersnets.services;

import com.writersnets.models.OperationType;
import com.writersnets.models.entities.*;
import com.writersnets.models.entities.books.Book;
import com.writersnets.models.entities.contests.Contest;
import com.writersnets.models.entities.users.*;
import com.writersnets.models.exceptions.NotEnoughMoneyException;
import com.writersnets.models.exceptions.ObjectNotFoundException;
import com.writersnets.models.exceptions.WrongDataException;
import com.writersnets.models.request.BuyRequest;
import com.writersnets.models.response.BalanceResponse;
import com.writersnets.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Created by mhenr on 26.01.2018.
 */
@Service
@Transactional(readOnly = true)
public class BalanceService {
    private BalanceRepository balanceRepository;
    private AuthorizedUserService authorizedUserService;
    private AuthorRepository authorRepository;
    private GiftRepository giftRepository;
    private BookRepository bookRepository;
    private UserBookRepository userBookRepository;
    private UserGiftRepository userGiftRepository;
    private ContestRepository contestRepository;

    @Autowired
    public BalanceService(final BalanceRepository balanceRepository, final AuthorizedUserService authorizedUserService,
                          final AuthorRepository authorRepository, final GiftRepository giftRepository,
                          final BookRepository bookRepository, final UserBookRepository userBookRepository,
                          final UserGiftRepository userGiftRepository, final ContestRepository contestRepository) {
        this.balanceRepository = balanceRepository;
        this.authorizedUserService = authorizedUserService;
        this.authorRepository = authorRepository;
        this.giftRepository = giftRepository;
        this.bookRepository = bookRepository;
        this.userBookRepository = userBookRepository;
        this.userGiftRepository = userGiftRepository;
        this.contestRepository = contestRepository;
    }

    public BalanceResponse getUserBalance() {
        Page<BalanceResponse> paymentHistory = getUserPaymentHistory(PageRequest.of(0, 1));
        if (!paymentHistory.getContent().isEmpty()) {
            return paymentHistory.getContent().get(0);
        }
        return new BalanceResponse();
    }

    public Page<BalanceResponse> getUserPaymentHistory(final Pageable pageable) {
        final User user = authorizedUserService.getAuthorizedUser();
        return balanceRepository.getUserPaymentHistory(user.getUsername(), pageable);
    }

    @Transactional
    public void processOperation(final BuyRequest buyRequest) {
        if (buyRequest == null || buyRequest.getOperationType() == null || buyRequest.getSourceUserId() == null) {
            throw new WrongDataException("BuyRequest should not be null");
        }
        switch(buyRequest.getOperationType()) {
            case BALANCE_RECHARGE:
                balanceRecharge(buyRequest.getSourceUserId(), buyRequest.getAmount());
                break;
            case PREMIUM_ACCOUNT:
                buyPremiumAccount(buyRequest.getSourceUserId(), buyRequest.getPurchaseId());
                break;
            case BOOK:
                buyBook(buyRequest.getSourceUserId(), buyRequest.getPurchaseId());
                break;
            case CONTEST_DONATE:
                donateToContest(buyRequest.getSourceUserId(), buyRequest.getPurchaseId(), buyRequest.getAmount());
                break;
            case MEDAL:
            case GIFT:
                transferGift(buyRequest);
                break;
        }
    }

    private void balanceRecharge(final String userId, final Long rechargeAmount) {
        final User user = authorRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("User is not found"));
        addToPaymentHistory(user, OperationType.BALANCE_RECHARGE, rechargeAmount, true);
    }

    private void buyPremiumAccount(final String userId, final Long purchaseId) {
        final User buyer = authorRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("User is not found"));
        final Gift gift = giftRepository.findById(purchaseId).orElseThrow(() -> new ObjectNotFoundException("Purchase is not found in database"));
        addToPaymentHistory(buyer, OperationType.PREMIUM_ACCOUNT, gift.getCost(), false);
        activatePremiumAccountFor(buyer);
    }

    private void activatePremiumAccountFor(final User user) {
        user.setPremium(true);
        user.setPremiumExpired(LocalDateTime.now().plusYears(1));
    }

    private void buyBook(final String userId, final Long bookId) {
        final User buyer = authorRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("User is not found"));
        final Book book = bookRepository.findById(bookId).orElseThrow(() -> new ObjectNotFoundException("Book is not found"));
        if (book.getCost() == null) {
            book.setCost(0L);
        }
        addToPaymentHistory(buyer, OperationType.BOOK, book.getCost(), false);
        addToPaymentHistory(book.getAuthor(), OperationType.BOOK, book.getCost(), true);
        addBookToUserPermission(buyer, book);
    }

    private void addBookToUserPermission(final User user, final Book book) {
        final UserBook userBook = new UserBook();
        final UserBookPK userBookPK = new UserBookPK();
        userBookPK.setUser(user);
        userBookPK.setBook(book);
        userBook.setUserBookPK(userBookPK);
        userBookRepository.save(userBook);
    }

    private void donateToContest(final String userId, final Long contestId, final Long amount) {
        final User donator = authorRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("User is not found"));
        final Contest contest = contestRepository.findById(contestId).orElseThrow(() -> new ObjectNotFoundException("Contest is not found"));
        if (contest.getClosed()) {
            throw new WrongDataException("You cannot donate to the closed contest");
        }
        addToPaymentHistory(donator, OperationType.CONTEST_DONATE, amount, false);
        addAmountToContest(contest, amount);
    }

    private void addAmountToContest(final Contest contest, final Long amount) {
        contest.setPrizeFund(contest.getPrizeFund() + amount);
        contestRepository.save(contest);
    }

    private void transferGift(final BuyRequest buyRequest) {
        final User sourceUser = authorRepository.findById(buyRequest.getSourceUserId()).orElseThrow(() -> new ObjectNotFoundException("User is not found"));
        final User destUser = authorRepository.findById(buyRequest.getDestUserId()).orElseThrow(() -> new ObjectNotFoundException("User is not found"));
        final Gift gift = giftRepository.findById(buyRequest.getPurchaseId()).orElseThrow(() -> new ObjectNotFoundException("Gift is not found in the database"));
        addToPaymentHistory(sourceUser, buyRequest.getOperationType(), gift.getCost(), false);
        addToPaymentHistory(destUser, buyRequest.getOperationType(), gift.getCost(), true);
        addGiftToUser(destUser, sourceUser, gift, buyRequest.getSendMessage());
    }

    private void addGiftToUser(final User user, final User sender, final Gift gift, final String message) {
        final UserGift userGift = new UserGift();
        userGift.setUser(user);
        userGift.setSender(sender);
        userGift.setGift(gift);
        userGift.setSendMessage(message);
        userGiftRepository.save(userGift);
    }

    private void addToPaymentHistory(final User user, final OperationType operationType, final Long operationCost, final boolean increase) {
        final Long totalCost = increase ? operationCost : operationCost * -1;
        final Long userBalance = user.getBalance();
        if (!increase && userBalance < operationCost) {
            throw new NotEnoughMoneyException("You haven't enough money to proceed with this operation");
        }
        final Billing billing = new Billing();
        billing.setAuthor(user);
        billing.setOperationType(operationType);
        billing.setOperationCost(totalCost);
        billing.setOperationDate(LocalDateTime.now());
        billing.setBalance(userBalance + totalCost);
        balanceRepository.save(billing);
        user.setBalance(billing.getBalance());
    }
}
