package org.booklink.services;

import org.booklink.models.OperationType;
import org.booklink.models.entities.Billing;
import org.booklink.models.entities.Gift;
import org.booklink.models.entities.User;
import org.booklink.models.exceptions.NotEnoughMoneyException;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.WrongDataException;
import org.booklink.models.request.BuyRequest;
import org.booklink.models.response.BalanceResponse;
import org.booklink.repositories.AuthorRepository;
import org.booklink.repositories.BalanceRepository;
import org.booklink.repositories.GiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

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

    @Autowired
    public BalanceService(final BalanceRepository balanceRepository, final AuthorizedUserService authorizedUserService,
                          final AuthorRepository authorRepository, final GiftRepository giftRepository) {
        this.balanceRepository = balanceRepository;
        this.authorizedUserService = authorizedUserService;
        this.authorRepository = authorRepository;
        this.giftRepository = giftRepository;
    }

    public BalanceResponse getUserBalance() {
        Page<BalanceResponse> paymentHistory = getUserPaymentHistory(new PageRequest(0, 1));
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
        if (buyRequest == null || buyRequest.getOperationType() == null || buyRequest.getSourceUserId() == null
                || buyRequest.getPurchaseId() == null) {
            throw new WrongDataException("BuyRequest should not be null");
        }
        switch(buyRequest.getOperationType()) {
            case BALANCE_RECHARGE:
                balanceRecharge(buyRequest.getSourceUserId(), buyRequest.getPurchaseId());
                break;
            case PREMIUM_ACCOUNT:
                buyPremiumAccount(buyRequest.getSourceUserId(), buyRequest.getPurchaseId());
                break;
            case BOOK:
            case MEDAL:
            case GIFT:
                transferGift(buyRequest.getOperationType(), buyRequest.getSourceUserId(), buyRequest.getDestUserId(), buyRequest.getPurchaseId());
                break;
        }
    }

    private void balanceRecharge(final String userId, final Long rechargeAmount) {
        final User user = authorRepository.findOne(userId);
        if (user == null) {
            throw new ObjectNotFoundException("User is not found");
        }
        addToPaymentHistory(user, OperationType.BALANCE_RECHARGE, rechargeAmount, true);
    }

    private void buyPremiumAccount(final String userId, final Long purchaseId) {
        final User buyer = authorRepository.findOne(userId);
        if (buyer == null) {
            throw new ObjectNotFoundException("User is not found");
        }
        final Gift gift = giftRepository.findOne(purchaseId);
        if (gift == null) {
            throw new ObjectNotFoundException("Purchase is not found in database");
        }
        addToPaymentHistory(buyer, OperationType.PREMIUM_ACCOUNT, gift.getCost(), false);
        activatePremiumAccountFor(buyer);
    }

    private void activatePremiumAccountFor(final User user) {
        user.setPremium(true);
        user.setPremiumStarted(new Date());
    }

    private void transferGift(final OperationType operationType, final String sourceUserId, final String destUserId, final Long purchaseId) {
        final User sourceUser = authorRepository.findOne(sourceUserId);
        final User destUser = authorRepository.findOne(destUserId);
        if (sourceUser == null || destUser == null) {
            throw new ObjectNotFoundException("User is not found");
        }
        final Gift gift = giftRepository.findOne(purchaseId);
        if (gift == null) {
            throw new ObjectNotFoundException("Gift is not found in the database");
        }
        addToPaymentHistory(sourceUser, operationType, gift.getCost(), false);
        addToPaymentHistory(destUser, operationType, gift.getCost(), true);
        //TODO: add gift to dest user
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
        billing.setOperationDate(new Date());
        billing.setBalance(userBalance + totalCost);
        balanceRepository.save(billing);
        user.setBalance(billing.getBalance());
    }
}
