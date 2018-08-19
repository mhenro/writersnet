package com.writersnets.models.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by mhenr on 09.03.2018.
 */
@NoArgsConstructor
@Getter
@Setter
public class ContestUserResponse {
    private String userId;
    private String userName;
    private Long contestId;
    private String contestName;
    private Long bookId;
    private String bookName;
    private Boolean accepted;

    /* for judges */
    public ContestUserResponse(final String userId, final String firstName, final String lastName,
                               final Long contestId, final String contestName, final Boolean accepted) {
        this.userId = userId;
        this.userName = firstName + " " + lastName;
        this.contestId = contestId;
        this.contestName = contestName;
        this.accepted = accepted;
    }

    /* for participants */
    public ContestUserResponse(final String userId, final String firstName, final String lastName,
                               final Long contestId, final String contestName, final Long bookId,
                               final String bookName, final Boolean accepted) {
        this.userId = userId;
        this.userName = firstName + " " + lastName;
        this.contestId = contestId;
        this.contestName = contestName;
        this.bookId = bookId;
        this.bookName = bookName;
        this.accepted = accepted;
    }
}
