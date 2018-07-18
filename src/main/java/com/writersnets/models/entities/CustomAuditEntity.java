package com.writersnets.models.entities;

import com.writersnets.utils.CustomRevisionListener;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "custom_audit_entity")
@RevisionEntity(CustomRevisionListener.class)
public class CustomAuditEntity {
    @NotNull
    @GenericGenerator(
            name = "revision_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "revision_id_sequence"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "revision_generator")
    @Column(updatable = false, nullable = false)
    @RevisionNumber
    private Integer id;

    @NotNull
    @RevisionTimestamp
    private Long timestamp;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private String userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomAuditEntity that = (CustomAuditEntity) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getTimestamp(), that.getTimestamp()) &&
                Objects.equals(getUserId(), that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTimestamp(), getUserId());
    }
}