package com.writersnets.models.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Created by mhenr on 25.02.2018.
 */
@NoArgsConstructor
@Getter
@Setter
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
    private LocalDateTime expirationDate;
    private Boolean started;
    private Boolean closed;
    private Boolean accepted;
    private Integer participantCount;
    private Integer judgeCount;
    private Long bookId;
    private String bookName;

    public ContestResponse(final Long id, final String name, final String creatorId, final String creatorFirstName, final String creatorLastName,
                           final Long prizeFund, final Integer firstPlaceRevenue, final Integer secondPlaceRevenue,
                           final Integer thirdPlaceRevenue, final LocalDateTime created, final LocalDateTime expirationDate, final Boolean started,
                           final Boolean closed, final Integer participantCount, final Integer judgeCount) {
        this(id, name, creatorId, creatorFirstName, creatorLastName, null, null, prizeFund, firstPlaceRevenue, secondPlaceRevenue, thirdPlaceRevenue,
                created, expirationDate, started, closed, null, participantCount, judgeCount);
    }

    public ContestResponse(final Long id, final String name, final String creatorId, final String creatorFirstName, final String creatorLastName,
                           final Long bookId, final String bookName, final Long prizeFund, final Integer firstPlaceRevenue, final Integer secondPlaceRevenue,
                           final Integer thirdPlaceRevenue, final LocalDateTime created, final LocalDateTime expirationDate, final Boolean started,
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
        this.expirationDate = expirationDate;
        this.started = started;
        this.closed = closed;
        this.accepted = accepted;
        this.judgeCount = judgeCount;
        this.participantCount = participantCount;
    }
}
