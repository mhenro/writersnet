package com.writersnets.models.entities;

import javax.persistence.Column;
import javax.persistence.Version;
import java.io.Serializable;

public class AbstractEntity implements Serializable {
    @Version
    @Column(name = "opt_lock")
    private Long version;
}
