package com.writersnets.models.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by mhenr on 10.01.2018.
 */
@Entity
@Table(name = "captcha")
public class Captcha extends AbstractEntity {
    @GenericGenerator(
            name = "captcha_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "captcha_seq_id"),
                    @Parameter(name = "initial_value", value = "0"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "captcha_generator")
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(unique = true)
    private String code;
    private LocalDateTime expired;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public LocalDateTime getExpired() {
        return expired;
    }
    public void setExpired(LocalDateTime expired) {
        this.expired = expired;
    }
}
