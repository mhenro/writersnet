package org.booklink.repositories;

import org.booklink.models.entities.ContestParticipant;
import org.booklink.models.entities.ContestParticipantPK;
import org.booklink.models.response.ContestUserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by mhenr on 09.03.2018.
 */
public interface ContestParticipantRepository extends PagingAndSortingRepository<ContestParticipant, ContestParticipantPK> {
    @Query("SELECT c.pk.participant.username FROM ContestParticipant c WHERE c.pk.contest.id = ?1")
    List<String> getParticipantsIdFromContest(final Long id);

    @Query("SELECT new org.booklink.models.response.ContestUserResponse(c.pk.participant.username, c.pk.participant.firstName, c.pk.participant.lastName, c.pk.contest.id, c.pk.contest.name, c.accepted) FROM ContestParticipant c WHERE c.pk.contest.id = ?1")
    Page<ContestUserResponse> getParticipantsFromContest(final Long id, final Pageable pageable);

    @Query("SELECT COUNT(*) FROM ContestParticipant c WHERE c.pk.contest.id = ?1")
    Long getParticipantCountFromContest(final Long id);

    @Query("SELECT COUNT(*) FROM ContestJudge c WHERE c.pk.contest.id = ?1 AND c.accepted = false")
    Long getNotAcceptedParticipantsFromContest(final Long id);

    @Query("SELECT COUNT(*) FROM ContestParticipant c WHERE c.pk.participant.username = ?1 AND c.accepted = false AND c.pk.contest.closed = false AND c.pk.contest.started = false")
    Long getNotAcceptedContestsByUser(final String userId);

    @Modifying
    @Query("DELETE FROM ContestParticipant c WHERE c.pk.contest.id = ?1")
    void clearParticipantsInContest(final Long id);
}
