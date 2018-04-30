package com.writersnets.repositories;

import com.writersnets.models.entities.UserGift;
import com.writersnets.models.response.UserGiftResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by mhenr on 10.02.2018.
 */
public interface UserGiftRepository extends PagingAndSortingRepository<UserGift, Long> {
    @Query("SELECT new com.writersnets.models.response.UserGiftResponse(g.id, g.gift, g.user.username, g.sender.username, g.sender.firstName, g.sender.lastName, g.sendMessage) FROM UserGift g WHERE g.user.username = ?1")
    Page<UserGiftResponse> getAuthorGifts(final String userId, final Pageable pageable);
}