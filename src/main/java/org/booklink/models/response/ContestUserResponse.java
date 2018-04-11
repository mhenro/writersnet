package org.booklink.models.response;

import org.booklink.models.entities.Contest;

/**
 * Created by mhenr on 09.03.2018.
 */
public class ContestUserResponse {
    private String userId;
    private String userName;
    private Long contestId;
    private String contestName;
    private Long bookId;
    private String bookName;
    private Boolean accepted;

    public ContestUserResponse(final String userId, final String firstName, final String lastName,
                               final Long contestId, final String contestName, final Boolean accepted) {
        this.userId = userId;
        this.userName = firstName + " " + lastName;
        this.contestId = contestId;
        this.contestName = contestName;
        this.accepted = accepted;
    }

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getContestId() {
        return contestId;
    }

    public void setContestId(Long contestId) {
        this.contestId = contestId;
    }

    public String getContestName() {
        return contestName;
    }

    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
}