package com.writersnets.models.entities.contests;

import com.writersnets.models.entities.AbstractIdEntity;
import com.writersnets.models.entities.users.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by mhenr on 25.02.2018.
 */
@Entity
@Table(name = "contests")
@Audited
@Getter @Setter @NoArgsConstructor
public class Contest extends AbstractIdEntity {
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

    public long getFirstRevenueAmount() {
        if (prizeFund == 0 || firstPlaceRevenue == 0) {
            return 0;
        }
        return (prizeFund * firstPlaceRevenue) / 100;
    }

    public long getSecondRevenueAmount() {
        if (prizeFund == 0 || secondPlaceRevenue == 0) {
            return 0;
        }
        return (prizeFund * secondPlaceRevenue) / 100;
    }

    public long getThirdRevenueAmount() {
        if (prizeFund == 0 || thirdPlaceRevenue == 0) {
            return 0;
        }
        return (prizeFund * thirdPlaceRevenue) / 100;
    }
}
