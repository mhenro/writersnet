package com.writersnets.repositories;

import com.writersnets.models.entities.Billing;
import com.writersnets.models.response.BalanceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by mhenr on 26.01.2018.
 */
public interface BalanceRepository extends PagingAndSortingRepository<Billing, Long> {
    @Query("SELECT new com.writersnets.models.response.BalanceResponse(b.id, b.author.username, b.operationType, b.operationDate, b.operationCost, b.balance) FROM Billing b WHERE b.author.username = ?1 ORDER BY b.operationDate DESC")
    Page<BalanceResponse> getUserPaymentHistory(final String userId, final Pageable pageable);
}
