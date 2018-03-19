package org.booklink.repositories;

import org.booklink.models.entities.Contest;
import org.booklink.models.response.ContestResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by mhenr on 25.02.2018.
 */
public interface ContestRepository extends PagingAndSortingRepository<Contest, Long> {
    @Query("SELECT new org.booklink.models.response.ContestResponse(c.id, c.name, c.creator.username, c.creator.firstName, c.creator.lastName, c.prizeFund, c.firstPlaceRevenue, c.secondPlaceRevenue, c.thirdPlaceRevenue, c.created, c.started) FROM Contest c ORDER BY c.created DESC")
    Page<ContestResponse> getAllContests(final Pageable pageable);

    @Query("SELECT new org.booklink.models.response.ContestResponse(c.id, c.name, c.creator.username, c.creator.firstName, c.creator.lastName, c.prizeFund, c.firstPlaceRevenue, c.secondPlaceRevenue, c.thirdPlaceRevenue, c.created, c.started) FROM Contest c WHERE c.id = ?1")
    ContestResponse getContest(final Long id);
}