package org.booklink.repositories;

import org.booklink.models.entities.ContestJudge;
import org.booklink.models.entities.ContestJudgePK;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by mhenr on 09.03.2018.
 */
public interface ContestJudgeRepository extends PagingAndSortingRepository<ContestJudge, ContestJudgePK> {
}
