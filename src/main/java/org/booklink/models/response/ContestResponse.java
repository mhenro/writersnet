package org.booklink.models.response;

import java.time.LocalDateTime;

/**
 * Created by mhenr on 25.02.2018.
 */
public class ContestResponse {
    private Long id;
    private String name;
    private String creatorId;
    private String creatorFullName;
    private Long prizeFund;
    private Integer firstPlaceRevenue;
    private Integer secondPlaceRevenue;
    private Integer thirdPlaceRevenue;
    private LocalDateTime created;
    private Boolean started;
    private Boolean closed;
    private Boolean accepted;
    private Integer participantCount;
    private Integer judgeCount;
    private Long bookId;
    private String bookName;

    public ContestResponse(final Long id, final String name, final String creatorId, final String creatorFirstName, final String creatorLastName,
                           final Long prizeFund, final Integer firstPlaceRevenue, final Integer secondPlaceRevenue,
                           final Integer thirdPlaceRevenue, final LocalDateTime created, final Boolean started,
                           final Boolean closed, final Integer participantCount, final Integer judgeCount) {
        this(id, name, creatorId, creatorFirstName, creatorLastName, null, null, prizeFund, firstPlaceRevenue, secondPlaceRevenue, thirdPlaceRevenue,
                created, started, closed, null, participantCount, judgeCount);
    }

    public ContestResponse(final Long id, final String name, final String creatorId, final String creatorFirstName, final String creatorLastName,
                           final Long bookId, final String bookName, final Long prizeFund, final Integer firstPlaceRevenue, final Integer secondPlaceRevenue,
                           final Integer thirdPlaceRevenue, final LocalDateTime created, final Boolean started,
                           final Boolean closed, final Boolean accepted, final Integer participantCount, final Integer judgeCount) {
        this.id = id;
        this.name = name;
        this.creatorId = creatorId;
        this.creatorFullName = creatorFirstName + " " + creatorLastName;
        this.bookId = bookId;
        this.bookName = bookName;
        this.prizeFund = prizeFund;
        this.firstPlaceRevenue = firstPlaceRevenue;
        this.secondPlaceRevenue = secondPlaceRevenue;
        this.thirdPlaceRevenue = thirdPlaceRevenue;
        this.created = created;
        this.started = started;
        this.closed = closed;
        this.accepted = accepted;
        this.judgeCount = judgeCount;
        this.participantCount = participantCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorFullName() {
        return creatorFullName;
    }

    public void setCreatorFullName(String creatorFullName) {
        this.creatorFullName = creatorFullName;
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

    public Long getPrizeFund() {
        return prizeFund;
    }

    public void setPrizeFund(Long prizeFund) {
        this.prizeFund = prizeFund;
    }

    public Integer getFirstPlaceRevenue() {
        return firstPlaceRevenue;
    }

    public void setFirstPlaceRevenue(Integer firstPlaceRevenue) {
        this.firstPlaceRevenue = firstPlaceRevenue;
    }

    public Integer getSecondPlaceRevenue() {
        return secondPlaceRevenue;
    }

    public void setSecondPlaceRevenue(Integer secondPlaceRevenue) {
        this.secondPlaceRevenue = secondPlaceRevenue;
    }

    public Integer getThirdPlaceRevenue() {
        return thirdPlaceRevenue;
    }

    public void setThirdPlaceRevenue(Integer thirdPlaceRevenue) {
        this.thirdPlaceRevenue = thirdPlaceRevenue;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Boolean getStarted() {
        return started;
    }

    public void setStarted(Boolean started) {
        this.started = started;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public Integer getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(Integer participantCount) {
        this.participantCount = participantCount;
    }

    public Integer getJudgeCount() {
        return judgeCount;
    }

    public void setJudgeCount(Integer judgeCount) {
        this.judgeCount = judgeCount;
    }
}
