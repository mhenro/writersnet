package org.booklink.models.request;

import java.time.LocalDateTime;

/**
 * Created by mhenr on 25.02.2018.
 */
public class ContestRequest {
    private Long id;
    private String name;
    private String creatorId;
    private Long prizeFund;
    private Integer firstPlaceRevenue;
    private Integer secondPlaceRevenue;
    private Integer thirdPlaceRevenue;
    private LocalDateTime expirationDate;

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

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }
}
