package org.booklink.services;

import org.booklink.models.entities.User;
import org.booklink.models.response.BalanceResponse;
import org.booklink.repositories.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mhenr on 26.01.2018.
 */
@Service
@Transactional(readOnly = true)
public class BalanceService {
    private BalanceRepository balanceRepository;
    private AuthorizedUserService authorizedUserService;

    @Autowired
    public BalanceService(final BalanceRepository balanceRepository, final AuthorizedUserService authorizedUserService) {
        this.balanceRepository = balanceRepository;
        this.authorizedUserService = authorizedUserService;
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
}
