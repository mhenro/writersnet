package com.writersnets.controllers.authors;

import com.writersnets.models.Response;
import com.writersnets.models.entities.users.Gift;
import com.writersnets.models.response.GiftResponse;
import com.writersnets.models.response.UserGiftResponse;
import com.writersnets.services.authors.GiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by mhenr on 27.01.2018.
 */
@RestController
@CrossOrigin
public class GiftController {
    private GiftService giftService;

    @Autowired
    public GiftController(final GiftService giftService) {
        this.giftService = giftService;
    }

    @RequestMapping(value = "gift/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getGift(@PathVariable final Long id) {
        final GiftResponse gift = giftService.getGift(id);
        return Response.createResponseEntity(0, gift, null, HttpStatus.OK);
    }

    @RequestMapping(value = "gifts", method = RequestMethod.GET)
    public ResponseEntity<?> getAllGifts() {
        final Map<String, List<Gift>> gifts = giftService.getAllGifts();
        return Response.createResponseEntity(0, gifts, null, HttpStatus.OK);
    }

    @RequestMapping(value = "gifts/authors/{userId}", method = RequestMethod.GET)
    public Page<UserGiftResponse> getAuthorGifts(@PathVariable final String userId, final Pageable pageable) {
        return giftService.getAuthorGifts(userId, pageable);
    }
}
