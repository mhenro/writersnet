package org.booklink.services;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by mhenr on 09.01.2018.
 */
@Service
public class CaptchaService {
    private final int CAPTCHA_WIDTH = 200;
    private final int CAPTCHA_HEIGHT = 40;
    private final int CAPTCHA_MAX_LENGTH = 4;
    private final int FONT_SIZE = 25;
    private final int LETTER_WIDTH = 14;
    private final char data[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'w', 'x', 'y', 'z'};

    public byte[] getCaptcha() throws IOException {
        final BufferedImage captcha = createCaptcha();
        return convertImageToByteArray(captcha);
    }

    private BufferedImage createCaptcha() {
        final BufferedImage captcha = new BufferedImage(CAPTCHA_WIDTH, CAPTCHA_HEIGHT, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = captcha.createGraphics();
        initializeBackground(graphics2D);
        renderCaptcha(graphics2D);
        graphics2D.dispose();

        return captcha;
    }

    private void renderCaptcha(final Graphics2D graphics2D) {
        final Font font = new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE);
        final StringBuffer buffer = getRandomBuffer();
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
