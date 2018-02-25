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

    public ContestResponse(final Long id, final String name, final String creatorId, final String creatorFirstName, final String creatorLastName,
                           final Long prizeFund, final Integer firstPlaceRevenue, final Integer secondPlaceRevenue,
                           final Integer thirdPlaceRevenue, final LocalDateTime created) {
        this.id = id;
        this.name = name;
        this.creatorId = creatorId;
        this.creatorFullName = creatorFirstName + " " + creatorLastName;
        this.prizeFund = prizeFund;
        this.firstPlaceRevenue = firstPlaceRevenue;
        this.secondPlaceRevenue = secondPlaceRevenue;
        this.thirdPlaceRevenue = thirdPlaceRevenue;
        this.created = created;
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
}