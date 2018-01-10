package org.booklink.services;

import org.booklink.models.request.FeedbackRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by mhenr on 10.01.2018.
 */
@Service
public class FeedbackService {
    private JavaMailSender mailSender;
    private Environment environment;

    @Autowired
    public FeedbackService(final JavaMailSender mailSender, final Environment environment) {
        this.mailSender = mailSender;
        this.environment = environment;
    }

    public void sendFeedbackEmail(final FeedbackRequest feedback) throws MessagingException {
        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        final String message = createHtmlTemplate(feedback.getName(), feedback.getSubject(), feedback.getMessage());
        mimeMessage.setHeader("Content-Type", "text/html; charset=UTF-8");
        mimeMessage.setContent(message, "text/html");
        helper.setTo(getDestinationEmail());
        helper.setSubject(feedback.getSubject());
        helper.setFrom(feedback.getEmail());
        mailSender.send(mimeMessage);
    }

    private String getDestinationEmail() {
        final String host = environment.getProperty("writersnet.mail");
        return host;
    }

    private String createHtmlTemplate(final String name, final String subject, final String message) {
        return "<!doctype html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <meta name=\"viewport\" content=\"width=device-width\">\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "    <title>Simple Transactional Email</title>\n" +
                "    <style>\n" +
                "    /* -------------------------------------\n" +
                "        INLINED WITH htmlemail.io/inline\n" +
                "    ------------------------------------- */\n" +
                "    /* -------------------------------------\n" +
                "        RESPONSIVE AND MOBILE FRIENDLY STYLES\n" +
                "    ------------------------------------- */\n" +
                "    @media only screen and (max-width: 620px) {\n" +
                "      table[class=body] h1 {\n" +
                "        font-size: 28px !important;\n" +
                "        margin-bottom: 10px !important;\n" +
                "      }\n" +
                "      table[class=body] p,\n" +
                "            table[class=body] ul,\n" +
                "            table[class=body] ol,\n" +
                "            table[class=body] td,\n" +
                "            table[class=body] span,\n" +
                "            table[class=body] a {\n" +
                "        font-size: 16px !important;\n" +
                "      }\n" +
                "      table[class=body] .wrapper,\n" +
                "            table[class=body] .article {\n" +
                "        padding: 10px !important;\n" +
                "      }\n" +
                "      table[class=body] .content {\n" +
                "        padding: 0 !important;\n" +
                "      }\n" +
                "      table[class=body] .container {\n" +
                "        padding: 0 !important;\n" +
                "        width: 100% !important;\n" +
                "      }\n" +
                "      table[class=body] .main {\n" +
                "        border-left-width: 0 !important;\n" +
                "        border-radius: 0 !important;\n" +
                "        border-right-width: 0 !important;\n" +
                "      }\n" +
                "      table[class=body] .btn table {\n" +
                "        width: 100% !important;\n" +
                "      }\n" +
                "      table[class=body] .btn a {\n" +
                "        width: 100% !important;\n" +
                "      }\n" +
                "      table[class=body] .img-responsive {\n" +
                "        height: auto !important;\n" +
                "        max-width: 100% !important;\n" +
                "        width: auto !important;\n" +
                "      }\n" +
                "    }\n" +
                "    /* -------------------------------------\n" +
                "        PRESERVE THESE STYLES IN THE HEAD\n" +
                "    ------------------------------------- */\n" +
                "    @media all {\n" +
                "      .ExternalClass {\n" +
                "        width: 100%;\n" +
                "      }\n" +
                "      .ExternalClass,\n" +
                "            .ExternalClass p,\n" +
                "            .ExternalClass span,\n" +
                "            .ExternalClass font,\n" +
                "            .ExternalClass td,\n" +
                "            .ExternalClass div {\n" +
                "        line-height: 100%;\n" +
                "      }\n" +
                "      .apple-link a {\n" +
                "        color: inherit !important;\n" +
                "        font-family: inherit !important;\n" +
                "        font-size: inherit !important;\n" +
                "        font-weight: inherit !important;\n" +
                "        line-height: inherit !important;\n" +
                "        text-decoration: none !important;\n" +
                "      }\n" +
                "      .btn-primary table td:hover {\n" +
                "        background-color: #34495e !important;\n" +
                "      }\n" +
                "      .btn-primary a:hover {\n" +
                "        background-color: #34495e !important;\n" +
                "        border-color: #34495e !important;\n" +
                "      }\n" +
                "    }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body class=\"\" style=\"background-color: #f6f6f6; font-family: sans-serif; -webkit-font-smoothing: antialiased; font-size: 14px; line-height: 1.4; margin: 0; padding: 0; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\">\n" +
                "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"body\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 100%; background-color: #f6f6f6;\">\n" +
                "      <tr>\n" +
                "        <td style=\"font-family: sans-serif; font-size: 14px; vertical-align: top;\">&nbsp;</td>\n" +
                "        <td class=\"container\" style=\"font-family: sans-serif; font-size: 14px; vertical-align: top; display: block; Margin: 0 auto; max-width: 580px; padding: 10px; width: 580px;\">\n" +
                "          <div class=\"content\" style=\"box-sizing: border-box; display: block; Margin: 0 auto; max-width: 580px; padding: 10px;\">\n" +
                "\n" +
                "            <!-- START CENTERED WHITE CONTAINER -->\n" +
                "            <span class=\"preheader\" style=\"color: transparent; display: none; height: 0; max-height: 0; max-width: 0; opacity: 0; overflow: hidden; mso-hide: all; visibility: hidden; width: 0;\">This is preheader text. Some clients will show this text as a preview.</span>\n" +
                "            <table class=\"main\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 100%; background: #ffffff; border-radius: 3px;\">\n" +
                "\n" +
                "              <!-- START MAIN CONTENT AREA -->\n" +
                "              <tr>\n" +
                "                <td class=\"wrapper\" style=\"font-family: sans-serif; font-size: 14px; vertical-align: top; box-sizing: border-box; padding: 20px;\">\n" +
                "                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 100%;\">\n" +
                "                    <tr>\n" +
                "                      <td style=\"font-family: sans-serif; font-size: 14px; vertical-align: top;\">\n" +
                "                        <p style=\"font-family: sans-serif; font-size: 14px; font-weight: normal; margin: 0; Margin-bottom: 15px;\">Name: " + name + "</p>\n" +
                "\t\t\t<br/>\n" +
                "                        <p style=\"font-family: sans-serif; font-size: 14px; font-weight: normal; margin: 0; Margin-bottom: 15px;\">Subject: " + subject + "</p>\n" +
                "\t\t\t<br/>\n" +
                "                        <p style=\"font-family: sans-serif; font-size: 14px; font-weight: normal; margin: 0; Margin-bottom: 15px;\">Message: " + message + "</p>\n" +
                "\t\t\t<br/>\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                  </table>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "\n" +
                "            <!-- END MAIN CONTENT AREA -->\n" +
                "            </table>\n" +
                "\n" +
                "            <!-- START FOOTER -->\n" +
                "            <div class=\"footer\" style=\"clear: both; Margin-top: 10px; text-align: center; width: 100%;\">\n" +
                "              <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 100%;\">\n" +
                "                <tr>\n" +
                "                  <td class=\"content-block powered-by\" style=\"font-family: sans-serif; vertical-align: top; padding-bottom: 10px; padding-top: 10px; font-size: 12px; color: #999999; text-align: center;\">\n" +
                "                    2018, WritersNets.com\n" +
                "                  </td>\n" +
                "                </tr>\n" +
                "              </table>\n" +
                "            </div>\n" +
                "            <!-- END FOOTER -->\n" +
                "\n" +
                "          <!-- END CENTERED WHITE CONTAINER -->\n" +
                "          </div>\n" +
                "        </td>\n" +
                "        <td style=\"font-family: sans-serif; font-size: 14px; vertical-align: top;\">&nbsp;</td>\n" +
                "      </tr>\n" +
                "    </table>\n" +
                "  </body>\n" +
                "</html>";
    }
}
