package com.writersnets.models.entities.groups;

import com.writersnets.models.entities.AbstractIdEntity;
import com.writersnets.models.entities.groups.ChatGroup;
import com.writersnets.models.entities.users.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by mhenr on 06.12.2017.
 */
@Entity
@Table(name = "messages")
@Audited
@Getter @Setter @NoArgsConstructor
public class Message extends AbstractIdEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @Column(name = "message_text")
    private String message;
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private ChatGroup group;
    private Boolean unread;

    /* --------------------------------------------business logic-------------------------------------------- */

    public String getCreatorFullName() {
        return creator.getFullName();
    }
}
