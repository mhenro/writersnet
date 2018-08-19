package com.writersnets.models.entities.users;

import com.writersnets.models.entities.AbstractIdEntity;
import com.writersnets.models.entities.users.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by mhenr on 20.12.2017.
 */
@Entity
@Table(name = "sessions")
@Getter @Setter @NoArgsConstructor
public class Session extends AbstractIdEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private User author;

    @Column(name = "expire_date")
    private LocalDateTime expired;
}
