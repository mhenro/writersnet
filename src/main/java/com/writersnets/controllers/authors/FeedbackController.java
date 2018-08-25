package com.writersnets.controllers.authors;

import com.writersnets.models.Response;
import com.writersnets.models.request.FeedbackRequest;
import com.writersnets.services.security.CaptchaService;
import com.writersnets.services.authors.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

/**
 * Created by mhenr on 10.01.2018.
 */
@RestController
@CrossOrigin
public class FeedbackController {
    private CaptchaService captchaService;
    private FeedbackService feedbackService;

    @Autowired
    public FeedbackController(final CaptchaService captchaService, final FeedbackService feedbackService) {
        this.captchaService = captchaService;
        this.feedbackService = feedbackService;
    }

    @RequestMapping(value = "feedback", method = RequestMethod.POST)
    public ResponseEntity<?> sendMessage(@RequestBody final FeedbackRequest feedback) throws MessagingException {
        captchaService.checkCaptchaCode(feedback.getCaptcha());
        feedbackService.sendFeedbackEmail(feedback);
        return Response.createResponseEntity(0, "Your message was sent successfully", null, HttpStatus.OK);
    }
}
