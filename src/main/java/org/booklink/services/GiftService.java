package org.booklink.services;

import org.booklink.models.entities.Gift;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.response.GiftResponse;
import org.booklink.repositories.GiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
    private Environment environment;

    @Autowired
    public GiftService(final GiftRepository giftRepository, final Environment environment) {
        this.giftRepository = giftRepository;
        this.environment = environment;
    }

    public GiftResponse getGift(final long id) {
        final Gift gift = giftRepository.findOne(id);
        if (gift == null) {
            throw new ObjectNotFoundException("Gift was not found");
        }
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

    private Gift setImage(final Gift gift) {
        final String defaultImage = environment.getProperty("writersnet.giftwebstorage.path") + "default_gift.png";
        if (gift.getImage() == null || gift.getImage().isEmpty()) {
            gift.setImage(defaultImage);
        }
        return gift;
    }
}
