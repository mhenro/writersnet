package org.booklink.repositories;

import org.booklink.models.entities.ContestJudge;
import org.booklink.models.entities.ContestJudgePK;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by mhenr on 09.03.2018.
 */
public interface ContestJudgeRepository extends PagingAndSortingRepository<ContestJudge, ContestJudgePK> {
    @Query("SELECT c.pk.judge.username FROM ContestJudge c WHERE c.pk.contest.id = ?1")
    List<String> getJudgesFromContest(final Long id);

    @Modifying
    @Query("DELETE FROM ContestJudge c WHERE c.pk.contest.id = ?1")
    void clearJudgesInContest(final Long id);
}
