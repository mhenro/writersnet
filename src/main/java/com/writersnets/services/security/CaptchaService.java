package com.writersnets.services.security;

import com.writersnets.models.entities.security.Captcha;
import com.writersnets.models.exceptions.WrongCaptchaException;
import com.writersnets.repositories.CaptchaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Created by mhenr on 09.01.2018.
 */
@Service
@Transactional
public class CaptchaService {
    private final int CAPTCHA_WIDTH = 200;
    private final int CAPTCHA_HEIGHT = 40;
    private final int CAPTCHA_MAX_LENGTH = 4;
    private final int FONT_SIZE = 25;
    private final int LETTER_WIDTH = 14;
    private final char data[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'w', 'x', 'y', 'z'};

    private CaptchaRepository captchaRepository;

    @Autowired
    public CaptchaService(final CaptchaRepository captchaRepository) {
        this.captchaRepository = captchaRepository;
    }

    public byte[] getCaptcha() throws IOException {
        final BufferedImage captcha = createCaptcha();
        return convertImageToByteArray(captcha);
    }

    public void checkCaptchaCode(final String code) {
        final Captcha captcha = captchaRepository.findByCode(code);
        if (captcha == null) {
            throw new WrongCaptchaException("Captcha code is incorrect");
        }
        captchaRepository.delete(captcha);
    }

    private BufferedImage createCaptcha() {
        final BufferedImage captcha = new BufferedImage(CAPTCHA_WIDTH, CAPTCHA_HEIGHT, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = captcha.createGraphics();
        final StringBuffer buffer = getRandomBuffer();
        initializeBackground(graphics2D);
        renderCaptcha(graphics2D, buffer);
        graphics2D.dispose();
        saveCaptchaInDB(buffer);

        return captcha;
    }

    private void saveCaptchaInDB(final StringBuffer buffer) {
        final Captcha captcha = new Captcha();
        captcha.setCode(buffer.toString());
        captcha.setExpired(getExpiredDate());
        captchaRepository.save(captcha);
    }

    private LocalDateTime getExpiredDate() {
        return LocalDateTime.now().plusMinutes(1);
    }

    @Scheduled(fixedDelay = 300000) //5 min
    public void removeOldCaptchaSessions() {
        captchaRepository.deleteOldCaptcha(LocalDateTime.now());
    }

    private void renderCaptcha(final Graphics2D graphics2D, final StringBuffer buffer) {
        final Font font = new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE);
        final Random r = new Random();
        graphics2D.setFont(font);
        graphics2D.setColor(new Color(255, 153, 0));
        int x = CAPTCHA_WIDTH/2 - CAPTCHA_MAX_LENGTH*LETTER_WIDTH;
        int y = 0;

        for (int i = 0; i < buffer.length(); i++) {
            x += 11 + (Math.abs(r.nextInt()) % 15);
            y = 20 + Math.abs(r.nextInt()) % 10;
            graphics2D.drawChars(buffer.toString().toCharArray(), i, 1, x, y);
        }
    }

    private void initializeBackground(final Graphics2D graphics2D) {
        final RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        graphics2D.setRenderingHints(rh);

        final GradientPaint gp = new GradientPaint(0, 0,
                Color.black, 0, CAPTCHA_HEIGHT/3, Color.red, true);

        graphics2D.setPaint(gp);
        graphics2D.fillRect(0, 0, CAPTCHA_WIDTH, CAPTCHA_HEIGHT);
    }

    private StringBuffer getRandomBuffer() {
        Random r = new Random();
        final StringBuffer buffer = new StringBuffer(10);
        for (int i = 0; i < CAPTCHA_MAX_LENGTH; i++) {
            buffer.append(data[r.nextInt(data.length)]);
        }
        return buffer;
    }

    private byte[] convertImageToByteArray(final BufferedImage image) throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);
        return os.toByteArray();
    }
}
