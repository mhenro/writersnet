package com.writersnets.models.entities;

import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;

@MappedSuperclass
@Audited
public class AbstractEntity implements Serializable {
    @Version
    @Column(name = "opt_lock")
    private Long version;
}
