package org.booklink.models.response;

import org.booklink.models.OperationType;
import org.booklink.models.entities.User;

import java.util.Date;

/**
 * Created by mhenr on 26.01.2018.
 */
public class BalanceResponse {
    private Long id;
    private String userId;
    private OperationType operationType;
    private Date operationDate;
    private Long operationCost;
    private Long balance;

    public BalanceResponse() {}

    public BalanceResponse(final Long id, final String userId, final OperationType operationType,
                           final Date operationDate, final Long operationCost, final Long balance) {
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

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
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
