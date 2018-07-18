package com.writersnets.models.entities;

import com.writersnets.models.GiftType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;

/**
 * Created by mhenr on 27.01.2018.
 */
@Entity
@Table(name = "gifts")
@Audited
@Getter @Setter @NoArgsConstructor
public class Gift extends AbstractIdEntity {
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "gift_type", nullable = false)
    private GiftType giftType;
    private Long cost;
    private String name;
    private String description;
    private String image;
    private String category;
}
