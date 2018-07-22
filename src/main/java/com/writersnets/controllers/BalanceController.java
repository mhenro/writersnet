package com.writersnets.controllers;

import com.writersnets.models.Response;
import com.writersnets.models.request.BuyRequest;
import com.writersnets.models.response.BalanceResponse;
import com.writersnets.services.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Created by mhenr on 26.01.2018.
 */
@RestController
@CrossOrigin
public class BalanceController {
    private BalanceService balanceService;

    @Autowired
    public BalanceController(final BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "balance", method = RequestMethod.GET)
    public ResponseEntity<?> getUserBalance() {
        final BalanceResponse balance = balanceService.getUserBalance();
        return Response.createResponseEntity(0, balance, null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "balance/history", method = RequestMethod.GET)
    public Page<BalanceResponse> getUserPaymentHistory(final Pageable pageable) {
        return balanceService.getUserPaymentHistory(pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "buy", method = RequestMethod.POST)
    public ResponseEntity<?> buy(@RequestBody final BuyRequest buyRequest) {
        balanceService.processOperation(buyRequest);
        return Response.createResponseEntity(0, "Operation was processed successfully", null, HttpStatus.OK);
    }
}
