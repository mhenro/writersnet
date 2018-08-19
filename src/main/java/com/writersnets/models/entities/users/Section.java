package com.writersnets.models.entities.users;

import com.writersnets.models.entities.AbstractIdEntity;
import com.writersnets.models.entities.users.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by mhenr on 20.10.2017.
 */
@Entity
@Table(name = "sections")
@Audited
@Getter @Setter @NoArgsConstructor
public class Section extends AbstractIdEntity {
    private String name;
    private String description;

    @Column(name = "last_update")
    private LocalDate lastUpdated;

    @OneToOne(mappedBy = "section", fetch = FetchType.LAZY)
    private User author;
}
