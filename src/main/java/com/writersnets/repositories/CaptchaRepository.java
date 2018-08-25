package com.writersnets.repositories;

import com.writersnets.models.entities.security.Captcha;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

/**
 * Created by mhenr on 10.01.2018.
 */
public interface CaptchaRepository extends CrudRepository<Captcha, Long> {
    Captcha findByCode(final String code);

    @Modifying
    @Query("DELETE FROM Captcha c WHERE c.expired < ?1")
    void deleteOldCaptcha(final LocalDateTime currentDate);
}
