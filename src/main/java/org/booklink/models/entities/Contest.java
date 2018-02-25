package org.booklink.models.entities;

import org.booklink.services.convertors.LocalDateTimeAttributeConverter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by mhenr on 25.02.2018.
 */
@Entity
public class Contest {
    private Long id;
    private String name;
    private User creator;
    private Long prizeFund;
    private Integer firstPlaceRevenue;
    private Integer secondPlaceRevenue;
    private Integer thirdPlaceRevenue;
    private LocalDateTime created;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator")
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @Column(name = "prize_fund")
    public Long getPrizeFund() {
        return prizeFund;
    }

    public void setPrizeFund(Long prizeFund) {
        this.prizeFund = prizeFund;
    }

    @Column(name = "first_place_revenue")
    public Integer getFirstPlaceRevenue() {
        return firstPlaceRevenue;
    }

    public void setFirstPlaceRevenue(Integer firstPlaceRevenue) {
        this.firstPlaceRevenue = firstPlaceRevenue;
    }

    @Column(name = "second_place_revenue")
    public Integer getSecondPlaceRevenue() {
        return secondPlaceRevenue;
    }

    public void setSecondPlaceRevenue(Integer secondPlaceRevenue) {
        this.secondPlaceRevenue = secondPlaceRevenue;
    }

    @Column(name = "third_place_revenue")
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
