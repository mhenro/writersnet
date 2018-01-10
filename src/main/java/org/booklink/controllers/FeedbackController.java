package org.booklink.controllers;

import org.booklink.models.Response;
import org.booklink.models.request.FeedbackRequest;
import org.booklink.services.CaptchaService;
import org.booklink.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

/**
 * Created by mhenr on 10.01.2018.
 */
@RestController
public class FeedbackController {
    private CaptchaService captchaService;
    private FeedbackService feedbackService;

    @Autowired
    public FeedbackController(final CaptchaService captchaService, final FeedbackService feedbackService) {
        this.captchaService = captchaService;
        this.feedbackService = feedbackService;
    }

    @CrossOrigin
    @RequestMapping(value = "feedback", method = RequestMethod.POST)
    public ResponseEntity<?> sendMessage(@RequestBody final FeedbackRequest feedback) {
        Response<String> response = new Response<>();
        if (captchaService.isCaptchaCorrect(feedback.getCaptcha())) {
            try {
                feedbackService.sendFeedbackEmail(feedback);
                response.setCode(0);
                response.setMessage("Your message was sent successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch(Exception e) {
                response.setCode(1);
                response.setMessage("Captcha code is incorrect");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }
        response.setCode(1);
        response.setMessage("Captcha code is incorrect");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /* ---------------------------------------exception handlers-------------------------------------- */

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<?> emailException(MessagingException e) {
        Response<String> response = new Response<>();
        response.setCode(1);
        response.setMessage("Problem with sending email. Please try again later.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
