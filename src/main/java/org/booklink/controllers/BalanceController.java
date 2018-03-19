package org.booklink.controllers;

import org.booklink.models.Response;
import org.booklink.models.exceptions.NotEnoughMoneyException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.exceptions.WrongDataException;
import org.booklink.models.request.BuyRequest;
import org.booklink.models.response.BalanceResponse;
import org.booklink.services.BalanceService;
import org.booklink.utils.ControllerHelper;
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
        final BalanceResponse balance = balanceService.getUserBalance();
        return Response.createResponseEntity(0, balance, null, HttpStatus.OK);
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
        balanceService.processOperation(buyRequest);
        return Response.createResponseEntity(0, "Operation was processed successfully", null, HttpStatus.OK);
    }

    /* ---------------------------------------exception handlers-------------------------------------- */

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<?> unauthorizedUser(UnauthorizedUserException e) {
        return Response.createResponseEntity(1, ControllerHelper.getErrorOrDefaultMessage(e, "Bad credentials"), null, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    public ResponseEntity<?> notEnoughMoney(NotEnoughMoneyException e) {
        return Response.createResponseEntity(1, ControllerHelper.getErrorOrDefaultMessage(e, "Not enough money for this operation"), null, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(WrongDataException.class)
    public ResponseEntity<?> wrongData(WrongDataException e) {
        return Response.createResponseEntity(1, ControllerHelper.getErrorOrDefaultMessage(e, "Wrong request data"), null, HttpStatus.BAD_REQUEST);
    }
}
