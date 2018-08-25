package com.writersnets.repositories;

import com.writersnets.models.entities.users.Gift;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by mhenr on 27.01.2018.
 */
public interface GiftRepository extends JpaRepository<Gift, Long> {
}
