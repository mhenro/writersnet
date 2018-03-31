package org.booklink.repositories;

import org.booklink.models.entities.ContestJudge;
import org.booklink.models.entities.ContestJudgePK;
import org.booklink.models.entities.User;
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
public interface ContestJudgeRepository extends PagingAndSortingRepository<ContestJudge, ContestJudgePK> {
    @Query("SELECT c.pk.judge.username FROM ContestJudge c WHERE c.pk.contest.id = ?1")
    List<String> getJudgesIdFromContest(final Long id);

    @Query("SELECT new org.booklink.models.response.ContestUserResponse(c.pk.judge.username, c.pk.judge.firstName, c.pk.judge.lastName, c.pk.contest.id, c.pk.contest.name, c.accepted) FROM ContestJudge c WHERE c.pk.contest.id = ?1")
    Page<ContestUserResponse> getJudgesFromContest(final Long id, final Pageable pageable);

    @Query("SELECT COUNT(*) FROM ContestJudge c WHERE c.pk.contest.id = ?1")
    Long getJudgeCountFromContest(final Long id);

    @Query("SELECT COUNT(*) FROM ContestJudge c WHERE c.pk.contest.id = ?1 AND c.accepted = false")
    Long getNotAcceptedJudgesFromContest(final Long id);

    @Query("SELECT COUNT(*) FROM ContestJudge c WHERE c.pk.judge.username = ?1 AND c.accepted = false AND c.pk.contest.closed = false AND c.pk.contest.started = false")
    Long getNotAcceptedContestsByUser(final String userId);

    @Query("SELECT c.pk.judge FROM ContestJudge c WHERE c.pk.contest.id = ?2 AND c.pk.judge.username = ?1")
    User getJudgeById(final String userId, final Long contestId);

    @Modifying
    @Query("UPDATE ContestJudge j SET j.accepted = true WHERE j.pk.judge.username = ?1 AND j.pk.contest.id = ?2")
    void joinInContest(final String userId, final Long contestId);

    @Modifying
    @Query("DELETE FROM ContestJudge c WHERE c.pk.contest.id = ?1")
    void clearJudgesInContest(final Long id);
}
