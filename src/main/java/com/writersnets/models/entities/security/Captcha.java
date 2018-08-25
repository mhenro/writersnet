package com.writersnets.models.entities.security;

import com.writersnets.models.entities.AbstractIdEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by mhenr on 10.01.2018.
 */
@Entity
@Table(name = "captcha")
@Getter @Setter @NoArgsConstructor
public class Captcha extends AbstractIdEntity {
    @Column(unique = true)
    private String code;
    private LocalDateTime expired;
}
