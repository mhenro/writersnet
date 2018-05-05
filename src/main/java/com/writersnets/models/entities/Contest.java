package com.writersnets.models.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by mhenr on 25.02.2018.
 */
@Entity
public class Contest extends AbstractEntity {
    @GenericGenerator(
            name = "contest_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "contest_id_seq"),
                    @Parameter(name = "initial_value", value = "0"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contest_generator")
    @Column(updatable = false, nullable = false)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator")
    private User creator;

    @Column(name = "prize_fund")
    private Long prizeFund;

    @Column(name = "first_place_revenue")
    private Integer firstPlaceRevenue;

    @Column(name = "second_place_revenue")
    private Integer secondPlaceRevenue;

    @Column(name = "third_place_revenue")
    private Integer thirdPlaceRevenue;
    private LocalDateTime created;
    private Boolean started;
    private Boolean closed;

    @Column(name = "judge_count")
    private Integer judgeCount;

    @Column(name = "participant_count")
    private Integer participantCount;

    @Column(name = "expiration_date")
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
    public User getCreator() {
        return creator;
    }
    public void setCreator(User creator) {
        this.creator = creator;
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
    public Integer getJudgeCount() {
        return judgeCount;
    }
    public void setJudgeCount(Integer judgeCount) {
        this.judgeCount = judgeCount;
    }
    public Integer getParticipantCount() {
        return participantCount;
    }
    public void setParticipantCount(Integer participantCount) {
        this.participantCount = participantCount;
    }
    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }
    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }
}