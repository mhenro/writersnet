package com.writersnets.models.entities.groups;

import com.writersnets.models.entities.AbstractIdEntity;
import com.writersnets.models.entities.users.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mhenr on 06.12.2017.
 */
@Entity
@Table(name = "chat_groups")
@Audited
@Getter @Setter @NoArgsConstructor
public class ChatGroup extends AbstractIdEntity {
    private String name;
    private String avatar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_recipient")
    private User primaryRecipient;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    @ManyToMany(mappedBy = "chatGroups", fetch = FetchType.LAZY)
    private List<User> users;

    @Column(name = "unread_by_creator")
    private Boolean unreadByCreator;

    @Column(name = "unread_by_recipient")
    private Boolean unreadByRecipient;
}
