package com.writersnets.models.entities;

import com.writersnets.models.OperationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by mhenr on 24.01.2018.
 */
@Entity
@Audited
@Getter @Setter @NoArgsConstructor
public class Billing extends AbstractIdEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "operation_type")
    private OperationType operationType;

    @Column(name = "operation_date")
    private LocalDateTime operationDate;

    @Column(name = "operation_cost", nullable = false)
    private Long operationCost;

    @Column(nullable = false)
    private Long balance;
}
