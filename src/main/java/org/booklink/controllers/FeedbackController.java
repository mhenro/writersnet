package org.booklink.controllers;

import org.booklink.models.Response;
import org.booklink.models.exceptions.WrongCaptchaException;
import org.booklink.models.request.FeedbackRequest;
import org.booklink.services.CaptchaService;
import org.booklink.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
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
    public ResponseEntity<?> sendMessage(@RequestBody final FeedbackRequest feedback) throws MessagingException {
        captchaService.checkCaptchaCode(feedback.getCaptcha());
        feedbackService.sendFeedbackEmail(feedback);
        return Response.createResponseEntity(0, "Your message was sent successfully", null, HttpStatus.OK);
    }

    /* ---------------------------------------exception handlers-------------------------------------- */

    @ExceptionHandler(WrongCaptchaException.class)
    public ResponseEntity<?> wrongCaptcha(WrongCaptchaException e) {
        return Response.createResponseEntity(1, e.getMessage().isEmpty() ? "Wrong captcha code" : e.getMessage(), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<?> emailException(MessagingException e) {
        return Response.createResponseEntity(1, "Problem with sending email. Please try again later. Reason: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
