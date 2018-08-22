package com.writersnets.models.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ContestRatingDetailsResponse {
    private String judgeId;
    private String judgeName;
    private Short estimation;

    public ContestRatingDetailsResponse(final String judgeId, final String judgeFirstName, final String judgeLastName, final Short estimation) {
        this.judgeId = judgeId;
        this.judgeName = judgeFirstName + " " + judgeLastName;
        this.estimation = estimation;
    }
}
