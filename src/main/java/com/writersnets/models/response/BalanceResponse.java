package com.writersnets.models.response;

import com.writersnets.models.OperationType;
import com.writersnets.models.entities.User;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by mhenr on 26.01.2018.
 */
public class BalanceResponse {
    private Long id;
    private String userId;
    private OperationType operationType;
    private LocalDateTime operationDate;
    private Long operationCost;
    private Long balance;

    public BalanceResponse() {}

    public BalanceResponse(final Long id, final String userId, final OperationType operationType,
                           final LocalDateTime operationDate, final Long operationCost, final Long balance) {
        this.id = id;
        this.userId = userId;
        this.operationType = operationType;
        this.operationDate = operationDate;
        this.operationCost = operationCost;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public LocalDateTime getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(LocalDateTime operationDate) {
        this.operationDate = operationDate;
    }

    public Long getOperationCost() {
        return operationCost;
    }

    public void setOperationCost(Long operationCost) {
        this.operationCost = operationCost;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }
}
