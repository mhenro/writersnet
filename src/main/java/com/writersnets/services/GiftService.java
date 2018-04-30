package com.writersnets.services;

import com.writersnets.models.entities.Gift;
import com.writersnets.models.exceptions.ObjectNotFoundException;
import com.writersnets.models.response.GiftResponse;
import com.writersnets.models.response.UserGiftResponse;
import com.writersnets.repositories.GiftRepository;
import com.writersnets.repositories.UserGiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by mhenr on 27.01.2018.
 */
@Service
public class GiftService {
    private GiftRepository giftRepository;
    private UserGiftRepository userGiftRepository;
    private Environment environment;

    @Autowired
    public GiftService(final GiftRepository giftRepository, final UserGiftRepository userGiftRepository, final Environment environment) {
        this.giftRepository = giftRepository;
        this.userGiftRepository = userGiftRepository;
        this.environment = environment;
    }

    public GiftResponse getGift(final long id) {
        final Gift gift = giftRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Gift was not found"));
        setImage(gift);
        final GiftResponse response = new GiftResponse(gift);
        return response;
    }

    public Map<String, List<Gift>> getAllGifts() {
        final List<Gift> gifts = giftRepository.findAll();
        return gifts.stream()
                .filter(gift -> gift.getCategory() != null && gift.getId() > 0)
                .map(this::setImage)
                .collect(Collectors.groupingBy(Gift::getCategory));
    }

    public Page<UserGiftResponse> getAuthorGifts(final String userId, final Pageable pageable) {
        Page<UserGiftResponse> result = userGiftRepository.getAuthorGifts(userId, pageable);
        result.forEach(this::setImage);
        return result;
    }

    private Gift setImage(final Gift gift) {
        final String defaultImage = environment.getProperty("writersnet.giftwebstorage.path") + "default_gift.png";
        if (gift.getImage() == null || gift.getImage().isEmpty()) {
            gift.setImage(defaultImage);
        }
        return gift;
    }

    private void setImage(final UserGiftResponse gift) {
        final String defaultImage = environment.getProperty("writersnet.giftwebstorage.path") + "default_gift.png";
        final GiftResponse giftResponse = gift.getGift();
        if (giftResponse.getImage() == null || giftResponse.getImage().isEmpty()) {
            giftResponse.setImage(defaultImage);
        }
    }
}
