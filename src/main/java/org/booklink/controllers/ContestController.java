package org.booklink.controllers;

import org.booklink.models.response.ContestResponse;
import org.booklink.services.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mhenr on 25.02.2018.
 */
@RestController
public class ContestController {
    private ContestService contestService;

    @Autowired
    public ContestController(final ContestService contestService) {
        this.contestService = contestService;
    }

    @CrossOrigin
    @RequestMapping(value = "contests", method = RequestMethod.GET)
    public Page<ContestResponse> getAllContests(final Pageable pageable) {
        return contestService.getAllContests(pageable);
    }
}
