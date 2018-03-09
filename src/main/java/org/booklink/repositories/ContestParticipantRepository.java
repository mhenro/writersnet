package org.booklink.repositories;

import org.booklink.models.entities.ContestParticipant;
import org.booklink.models.entities.ContestParticipantPK;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by mhenr on 09.03.2018.
 */
public interface ContestParticipantRepository extends PagingAndSortingRepository<ContestParticipant, ContestParticipantPK> {
    @Query("SELECT c.pk.participant.username FROM ContestParticipant c WHERE c.pk.contest.id = ?1")
    List<String> getParticipantsFromContest(final Long id);

    @Modifying
    @Query("DELETE FROM ContestParticipant c WHERE c.pk.contest.id = ?1")
    void clearParticipantsInContest(final Long id);
}
