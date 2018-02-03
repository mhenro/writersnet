package org.booklink.controllers;

import org.booklink.models.Response;
import org.booklink.services.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Created by mhenr on 09.01.2018.
 */
@RestController
public class CaptchaController {
    private CaptchaService captchaService;

    @Autowired
    public CaptchaController(final CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @CrossOrigin
    @RequestMapping(value = "captcha", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> getCaptcha() throws IOException {
        final HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        final byte[] captcha = captchaService.getCaptcha();
        return new ResponseEntity<>(captcha, headers, HttpStatus.OK);
    }

    /* ---------------------------------------exception handlers-------------------------------------- */

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> ioException(IOException e) {
        return Response.createResponseEntity(1, "Problem with captcha, try again later. Reason: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
