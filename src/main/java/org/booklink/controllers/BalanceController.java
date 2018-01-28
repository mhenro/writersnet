package org.booklink.controllers;

import org.booklink.models.Response;
import org.booklink.models.exceptions.NotEnoughMoneyException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.exceptions.WrongDataException;
import org.booklink.models.request.BuyRequest;
import org.booklink.models.response.BalanceResponse;
import org.booklink.services.BalanceService;
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
public class BalanceController {
    private BalanceService balanceService;

    @Autowired
    public BalanceController(final BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "balance", method = RequestMethod.GET)
    public ResponseEntity<?> getUserBalance() {
        final Response<BalanceResponse> response = new Response<>();
        final BalanceResponse balance = balanceService.getUserBalance();
        response.setCode(0);
        response.setMessage(balance);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "balance/history", method = RequestMethod.GET)
    public Page<BalanceResponse> getUserPaymentHistory(final Pageable pageable) {
        return balanceService.getUserPaymentHistory(pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "buy", method = RequestMethod.POST)
    public ResponseEntity<?> buy(@RequestBody final BuyRequest buyRequest) {
        final Response<String> response = new Response<>();
        balanceService.processOperation(buyRequest);
        response.setCode(0);
        response.setMessage("Operation was processed successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /* ---------------------------------------exception handlers-------------------------------------- */

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<?> unauthorizedUser(UnauthorizedUserException e) {
        Response<String> response = new Response<>();
        response.setCode(1);
        response.setMessage(e.getMessage().isEmpty() ? "Bad credentials" : e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    public ResponseEntity<?> notEnoughMoney(NotEnoughMoneyException e) {
        Response<String> response = new Response<>();
        response.setCode(1);
        response.setMessage(e.getMessage().isEmpty() ? "Not enough money for this operation" : e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(WrongDataException.class)
    public ResponseEntity<?> wrongData(WrongDataException e) {
        Response<String> response = new Response<>();
        response.setCode(1);
        response.setMessage(e.getMessage().isEmpty() ? "Wrong request data" : e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
