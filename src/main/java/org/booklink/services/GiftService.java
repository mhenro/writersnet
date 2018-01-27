package org.booklink.services;

import org.booklink.models.entities.Gift;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.response.GiftResponse;
import org.booklink.repositories.GiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by mhenr on 27.01.2018.
 */
@Service
public class GiftService {
    private GiftRepository giftRepository;

    @Autowired
    public GiftService(final GiftRepository giftRepository) {
        this.giftRepository = giftRepository;
    }

    public GiftResponse getGift(final long id) {
        final Gift gift = giftRepository.findOne(id);
        if (gift == null) {
            throw new ObjectNotFoundException("Gift was not found");
        }
        final GiftResponse response = new GiftResponse(gift);
        return response;
    }
}
