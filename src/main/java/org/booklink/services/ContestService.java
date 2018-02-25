package org.booklink.services;

import org.booklink.models.response.ContestResponse;
import org.booklink.repositories.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mhenr on 25.02.2018.
 */
@Service
@Transactional(readOnly = true)
public class ContestService {
    private ContestRepository contestRepository;

    @Autowired
    public ContestService(final ContestRepository contestRepository) {
        this.contestRepository = contestRepository;
    }

    public Page<ContestResponse> getAllContests(final Pageable pageable) {
        return contestRepository.getAllContests(pageable);
    }
}
