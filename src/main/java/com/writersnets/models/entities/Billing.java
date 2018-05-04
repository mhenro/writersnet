package com.writersnets.models.entities;

import com.writersnets.models.OperationType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by mhenr on 24.01.2018.
 */
@Entity
public class Billing extends AbstractEntity {
    @GenericGenerator(
            name = "billing_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "billing_id_seq"),
                    @Parameter(name = "initial_value", value = "0"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "billing_generator")
    @Column(updatable = false, nullable = false)
    private Long id;

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


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public User getAuthor() {
        return author;
    }
    public void setAuthor(User author) {
        this.author = author;
    }
    public OperationType getOperationType() {
        return operationType;
    }
    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }
    public LocalDateTime getOperationDate() {
        return operationDate;
    }
    public void setOperationDate(LocalDateTime operationDate) {
        this.operationDate = operationDate;
    }
    public Long getOperationCost() {
        return operationCost;
    }
    public void setOperationCost(Long operationCost) {
        this.operationCost = operationCost;
    }
    public Long getBalance() {
        return balance;
    }
    public void setBalance(Long balance) {
        this.balance = balance;
    }
}
