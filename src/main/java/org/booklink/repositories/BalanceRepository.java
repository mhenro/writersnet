package org.booklink.repositories;

import org.booklink.models.entities.Billing;
import org.booklink.models.response.BalanceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by mhenr on 26.01.2018.
 */
public interface BalanceRepository extends PagingAndSortingRepository<Billing, Long> {
    @Query("SELECT new org.booklink.models.response.BalanceResponse(b.id, b.author.username, b.operationType, b.operationDate, b.operationCost, b.balance) FROM Billing b WHERE b.author.username = ?1 ORDER BY b.operationDate DESC")
    Page<BalanceResponse> getUserPaymentHistory(final String userId, final Pageable pageable);
}
