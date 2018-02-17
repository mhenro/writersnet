package org.booklink.repositories;

import org.booklink.models.entities.Gift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by mhenr on 27.01.2018.
 */
public interface GiftRepository extends JpaRepository<Gift, Long> {
}
