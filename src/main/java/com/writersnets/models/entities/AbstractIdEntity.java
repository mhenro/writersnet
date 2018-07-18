package com.writersnets.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@Audited
@NoArgsConstructor
public class AbstractIdEntity implements Serializable {
    @GenericGenerator(
            name = "custom_id_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "custom_id_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom_id_generator")
    @Column(updatable = false, nullable = false)
    @Getter
    @Setter
    private Long id;

    @Version
    @Column(name = "opt_lock")
    private Long version;
}
