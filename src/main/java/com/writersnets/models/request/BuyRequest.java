package com.writersnets.models.request;

import com.writersnets.models.OperationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * Created by mhenr on 29.01.2018.
 */
@NoArgsConstructor
@Getter
@Setter
public class BuyRequest {
    @NotNull
    private OperationType operationType;

    private Long purchaseId;

    @NotNull
    private String sourceUserId;

    private String destUserId;

    private String sendMessage;

    private Long amount;
}