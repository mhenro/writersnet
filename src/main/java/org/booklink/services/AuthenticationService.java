package org.booklink.services;

import org.booklink.models.entities.Section;
import org.booklink.models.entities.User;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.request.Credentials;
import org.booklink.repositories.UserRepository;
import org.booklink.utils.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.Date;

import static org.booklink.utils.SecurityHelper.generateActivationToken;

/**
 * Created by mhenr on 14.11.2017.
 */
@Service
@Transactional(readOnly = true)
public class AuthenticationService {
    private final int DEFAULT_PASSWORD_LENGTH = 12;

    private UserRepository userRepository;
    private JavaMailSender mailSender;
    private Environment environment;

    @Autowired
    public AuthenticationService(final UserRepository userRepository, final JavaMailSender mailSender, final Environment environment) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.environment = environment;
    }

    public String auth(final Credentials credentials) {
        String result = null;
        final User user = userRepository.findOne(credentials.getUsername());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (user != null && passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
            final String key = environment.getProperty("jwt.signing-key");
            result = generateActivationToken(user, key);
        }
        return result;
    }

    @Transactional
    public boolean activate(final String token) {
        User user = userRepository.findUserByActivationToken(token);
        if (user != null) {
            user.setEnabled(true);
            user.setActivationToken("");
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean register(final Credentials credentials) throws MessagingException {
        User user = userRepository.findOne(credentials.getUsername());
        User userByEmail = userRepository.findUserByEmail(credentials.getEmail());
        if (user != null || userByEmail != null) {
            return false;
        }
        final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        final String password = passwordEncoder.encode(credentials.getPassword());
        user = new User();
        user.setEmail(credentials.getEmail());
        user.setUsername(credentials.getUsername());
        user.setPassword(password);
        user.setEnabled(false);
        user.setLanguage("EN");
        user.setAuthority("ROLE_USER");
        user.setOnline(false);

        createRegistrationLink(user);

        return true;
    }

    @Transactional
    public void passwordChangeConfirmation(final String email) throws MessagingException{
        if (email == null) {
            throw new ObjectNotFoundException("Email does not exist");
        }
        final User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new ObjectNotFoundException("This email doesn't exist in the database");
        }
        final String activationToken = setActivationTokenForUser(user);
        sendChangePasswordConfirmationEmail(email, user.getFullName(), activationToken);
    }

    private String setActivationTokenForUser(final User user) {
        final String key = environment.getProperty("jwt.signing-key");
        final String token = generateActivationToken(user, key);
        user.setActivationToken(token);
        userRepository.save(user);
        return token;
    }

    @Transactional
    public void setDefaultPassword(final String token, final String email) throws MessagingException {
        if (token == null) {
            throw new ObjectNotFoundException("Token does not exist");
        }
        final User user = userRepository.findUserByActivationToken(token);
        if (user == null) {
            throw new ObjectNotFoundException("No one user is related to this token");
        }
        final String newPassword = setDefaultPasswordForUser(user);
        sendPasswordReminder(email, user.getFullName(), newPassword);
    }

    private void createRegistrationLink(final User user) throws MessagingException {
        final String key = environment.getProperty("jwt.signing-key");
        final String token = generateActivationToken(user, key);
        final Section defaultSection = createDefaultSection(user);
        user.setActivationToken(token);
        user.setSection(defaultSection);
        userRepository.save(user);

        sendRegistrationEmail(user.getEmail(), user.getUsername(), token);
    }

    private Section createDefaultSection(final User user) {
        final Section section = new Section();
        section.setAuthor(user);
        section.setLastUpdated(LocalDate.now());
        section.setName("Author section");
        return section;
    }

    private String setDefaultPasswordForUser(final User user) {
        final RandomString randomString = new RandomString(DEFAULT_PASSWORD_LENGTH);
        final String newPassword = randomString.nextString();
        final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        final String password = passwordEncoder.encode(newPassword);
        user.setPassword(password);
        user.setActivationToken("");
        userRepository.save(user);
        return newPassword;
    }

    private void sendPasswordReminder(final String to, final String fullName, final String newPassword) throws MessagingException {
        final String mailAddr = environment.getProperty("writersnet.mail");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        String htmlMsg = "<!doctype html>" +
                "<html>" +
                "  <head>" +
                "    <meta name=\"viewport\" content=\"width=device-width\" />" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />" +
                "    <title>Password reminder from WritersNets.com</title>" +
                "  </head>" +
                "  <body>" +
                "Hello, " + fullName + "<br/>" +
                "You asked to remind you of your password.<br/>" +
                "Your new password is <b>" + newPassword + "</b>.<br/>" +
                "Change this password asap.<br/>" +
                "<br/><br/>" +
                "<a href=\"https://writersnets.com/\">2018, WritersNets.com</a>" +
                "  </body>" +
                "</html>";
        mimeMessage.setHeader("Content-Type", "text/html; charset=UTF-8");
        mimeMessage.setContent(htmlMsg, "text/html");
        helper.setTo(to);
        helper.setSubject("Password reminder from WritersNets.com");
        helper.setFrom(mailAddr);
        mailSender.send(mimeMessage);
    }

    private void sendRegistrationEmail(final String to, final String username, final String activationToken) throws MessagingException {
        final String host = environment.getProperty("writersnet.addr");
        final String mailAddr = environment.getProperty("writersnet.mail");
        String link = "<a href=\"" + host + "activate?activationToken=" + activationToken + "\" target=\"_blank\">Register on WritersNets.com</a>";
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        String htmlMsg = "<!doctype html>" +
                "<html>" +
                "  <head>" +
                "    <meta name=\"viewport\" content=\"width=device-width\" />" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8" +
                "\" />" +
                "    <title>Registration on WritersNets.com</title>" +
                "    <style>" +
                "      /* -------------------------------------" +
                "          GLOBAL RESETS" +
                "      ------------------------------------- */" +
                "      img {" +
                "        border: none;" +
                "        -ms-interpolation-mode: bicubic;" +
                "        max-width: 100%; }" +
                "" +
                "      body {" +
                "        background-color: #f6f6f6;" +
                "        font-family: sans-serif;" +
                "        -webkit-font-smoothing: antialiased;" +
                "        font-size: 14px;" +
                "        line-height: 1.4;" +
                "        margin: 0;" +
                "        padding: 0;" +
                "        -ms-text-size-adjust: 100%;" +
                "        -webkit-text-size-adjust: 100%; }" +
                "" +
                "      table {" +
                "        border-collapse: separate;" +
                "        mso-table-lspace: 0pt;" +
                "        mso-table-rspace: 0pt;" +
                "        width: 100%; }" +
                "        table td {" +
                "          font-family: sans-serif;" +
                "          font-size: 14px;" +
                "          vertical-align: top; }" +
                "" +
                "      /* -------------------------------------" +
                "          BODY & CONTAINER" +
                "      ------------------------------------- */" +
                "" +
                "      .body {" +
                "        background-color: #f6f6f6;" +
                "        width: 100%; }" +
                "      .container {" +
                "        display: block;" +
                "        Margin: 0 auto !important;" +
                "        /* makes it centered */" +
                "        max-width: 580px;" +
                "        padding: 10px;" +
                "        width: 580px; }" +
                "      .content {" +
                "        box-sizing: border-box;" +
                "        display: block;" +
                "        Margin: 0 auto;" +
                "        max-width: 580px;" +
                "        padding: 10px; }" +
                "" +
                "      /* -------------------------------------" +
                "          HEADER, FOOTER, MAIN" +
                "      ------------------------------------- */" +
                "      .main {" +
                "        background: #ffffff;" +
                "        border-radius: 3px;" +
                "        width: 100%; }" +
                "" +
                "      .wrapper {" +
                "        box-sizing: border-box;" +
                "        padding: 20px; }" +
                "" +
                "      .content-block {" +
                "        padding-bottom: 10px;" +
                "        padding-top: 10px;" +
                "      }" +
                "" +
                "      .footer {" +
                "        clear: both;" +
                "        Margin-top: 10px;" +
                "        text-align: center;" +
                "        width: 100%; }" +
                "        .footer td," +
                "        .footer p," +
                "        .footer span," +
                "        .footer a {" +
                "          color: #999999;" +
                "          font-size: 12px;" +
                "          text-align: center; }" +
                "" +
                "      /* -------------------------------------" +
                "          TYPOGRAPHY" +
                "      ------------------------------------- */" +
                "      h1," +
                "      h2," +
                "      h3," +
                "      h4 {" +
                "        color: #000000;" +
                "        font-family: sans-serif;" +
                "        font-weight: 400;" +
                "        line-height: 1.4;" +
                "        margin: 0;" +
                "        Margin-bottom: 30px; }" +
                "" +
                "      h1 {" +
                "        font-size: 35px;" +
                "        font-weight: 300;" +
                "        text-align: center;" +
                "        text-transform: capitalize; }" +
                "" +
                "      p," +
                "      ul," +
                "      ol {" +
                "        font-family: sans-serif;" +
                "        font-size: 14px;" +
                "        font-weight: normal;" +
                "        margin: 0;" +
                "        Margin-bottom: 15px; }" +
                "        p li," +
                "        ul li," +
                "        ol li {" +
                "          list-style-position: inside;" +
                "          margin-left: 5px; }" +
                "" +
                "      a {" +
                "        color: #3498db;" +
                "        text-decoration: underline; }" +
                "" +
                "      /* -------------------------------------" +
                "          BUTTONS" +
                "      ------------------------------------- */" +
                "      .btn {" +
                "        box-sizing: border-box;" +
                "        width: 100%; }" +
                "        .btn > tbody > tr > td {" +
                "          padding-bottom: 15px; }" +
                "        .btn table {" +
                "          width: auto; }" +
                "        .btn table td {" +
                "          background-color: #ffffff;" +
                "          border-radius: 5px;" +
                "          text-align: center; }" +
                "        .btn a {" +
                "          background-color: #ffffff;" +
                "          border: solid 1px #3498db;" +
                "          border-radius: 5px;" +
                "          box-sizing: border-box;" +
                "          color: #3498db;" +
                "          cursor: pointer;" +
                "          display: inline-block;" +
                "          font-size: 14px;" +
                "          font-weight: bold;" +
                "          margin: 0;" +
                "          padding: 12px 25px;" +
                "          text-decoration: none;" +
                "          text-transform: capitalize; }" +
                "" +
                "      .btn-primary table td {" +
                "        background-color: #3498db; }" +
                "" +
                "      .btn-primary a {" +
                "        background-color: #3498db;" +
                "        border-color: #3498db;" +
                "        color: #ffffff; }" +
                "" +
                "      /* -------------------------------------" +
                "          OTHER STYLES THAT MIGHT BE USEFUL" +
                "      ------------------------------------- */" +
                "      .last {" +
                "        margin-bottom: 0; }" +
                "" +
                "      .first {" +
                "        margin-top: 0; }" +
                "" +
                "      .align-center {" +
                "        text-align: center; }" +
                "" +
                "      .align-right {" +
                "        text-align: right; }" +
                "" +
                "      .align-left {" +
                "        text-align: left; }" +
                "" +
                "      .clear {" +
                "        clear: both; }" +
                "" +
                "      .mt0 {" +
                "        margin-top: 0; }" +
                "" +
                "      .mb0 {" +
                "        margin-bottom: 0; }" +
                "" +
                "      .preheader {" +
                "        color: transparent;" +
                "        display: none;" +
                "        height: 0;" +
                "        max-height: 0;" +
                "        max-width: 0;" +
                "        opacity: 0;" +
                "        overflow: hidden;" +
                "        mso-hide: all;" +
                "        visibility: hidden;" +
                "        width: 0; }" +
                "" +
                "      .powered-by a {" +
                "        text-decoration: none; }" +
                "" +
                "      hr {" +
                "        border: 0;" +
                "        border-bottom: 1px solid #f6f6f6;" +
                "        Margin: 20px 0; }" +
                "" +
                "      /* -------------------------------------" +
                "          RESPONSIVE AND MOBILE FRIENDLY STYLES" +
                "      ------------------------------------- */" +
                "      @media only screen and (max-width: 620px) {" +
                "        table[class=body] h1 {" +
                "          font-size: 28px !important;" +
                "          margin-bottom: 10px !important; }" +
                "        table[class=body] p," +
                "        table[class=body] ul," +
                "        table[class=body] ol," +
                "        table[class=body] td," +
                "        table[class=body] span," +
                "        table[class=body] a {" +
                "          font-size: 16px !important; }" +
                "        table[class=body] .wrapper," +
                "        table[class=body] .article {" +
                "          padding: 10px !important; }" +
                "        table[class=body] .content {" +
                "          padding: 0 !important; }" +
                "        table[class=body] .container {" +
                "          padding: 0 !important;" +
                "          width: 100% !important; }" +
                "        table[class=body] .main {" +
                "          border-left-width: 0 !important;" +
                "          border-radius: 0 !important;" +
                "          border-right-width: 0 !important; }" +
                "        table[class=body] .btn table {" +
                "          width: 100% !important; }" +
                "        table[class=body] .btn a {" +
                "          width: 100% !important; }" +
                "        table[class=body] .img-responsive {" +
                "          height: auto !important;" +
                "          max-width: 100% !important;" +
                "          width: auto !important; }}" +
                "" +
                "      /* -------------------------------------" +
                "          PRESERVE THESE STYLES IN THE HEAD" +
                "      ------------------------------------- */" +
                "      @media all {" +
                "        .ExternalClass {" +
                "          width: 100%; }" +
                "        .ExternalClass," +
                "        .ExternalClass p," +
                "        .ExternalClass span," +
                "        .ExternalClass font," +
                "        .ExternalClass td," +
                "        .ExternalClass div {" +
                "          line-height: 100%; }" +
                "        .apple-link a {" +
                "          color: inherit !important;" +
                "          font-family: inherit !important;" +
                "          font-size: inherit !important;" +
                "          font-weight: inherit !important;" +
                "          line-height: inherit !important;" +
                "          text-decoration: none !important; }" +
                "        .btn-primary table td:hover {" +
                "          background-color: #34495e !important; }" +
                "        .btn-primary a:hover {" +
                "          background-color: #34495e !important;" +
                "          border-color: #34495e !important; } }" +
                "" +
                "    </style>" +
                "  </head>" +
                "  <body class=\"\">" +
                "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"body\">" +
                "      <tr>" +
                "        <td>&nbsp;</td>" +
                "        <td class=\"container\">" +
                "          <div class=\"content\">" +
                "" +
                "            <!-- START CENTERED WHITE CONTAINER -->" +
                "            <span class=\"preheader\">Registration on WritersNets.com</span" +
                ">" +
                "            <table class=\"main\">" +
                "" +
                "              <!-- START MAIN CONTENT AREA -->" +
                "              <tr>" +
                "                <td class=\"wrapper\">" +
                "                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">" +
                "                    <tr>" +
                "                      <td>" +
                "                        <p>Hello " + username + ",</p>" +
                "                        <p>You have been registered on <b>WritersNets.com</b>.</p>" +
                "                        <p>To confirm the registration you need to click on" +
                " the following button:</p>" +
                "                        <table border=\"0\" cellpadding=\"0\" cellspacing" +
                "=\"0\" class=\"btn btn-primary\">" +
                "                          <tbody>" +
                "                            <tr>" +
                "                              <td align=\"center\">" +
                "                                <table border=\"0\" cellpadding=\"0\" cells" +
                "pacing=\"0\">" +
                "                                  <tbody>" +
                "                                    <tr>" +
                "                                      <td>" + link + "</td>" +
                "                                    </tr>" +
                "                                  </tbody>" +
                "                                </table>" +
                "                              </td>" +
                "                            </tr>" +
                "                          </tbody>" +
                "                        </table>" +
                "                      </td>" +
                "                    </tr>" +
                "                  </table>" +
                "                </td>" +
                "              </tr>" +
                "" +
                "            <!-- END MAIN CONTENT AREA -->" +
                "            </table>" +
                "" +
                "            <!-- START FOOTER -->" +
                "            <div class=\"footer\">" +
                "              <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">" +
                "                <tr>" +
                "                  <td class=\"content-block powered-by\">" +
                "                    (c) 2018, <a href=\"https://writersnets.com\">WritersNets.com</a>" +
                "                  </td>" +
                "                </tr>" +
                "              </table>" +
                "            </div>" +
                "            <!-- END FOOTER -->" +
                "" +
                "          <!-- END CENTERED WHITE CONTAINER -->" +
                "          </div>" +
                "        </td>" +
                "        <td>&nbsp;</td>" +
                "      </tr>" +
                "    </table>" +
                "  </body>" +
                "</html>";
        mimeMessage.setHeader("Content-Type", "text/html; charset=UTF-8");
        mimeMessage.setContent(htmlMsg, "text/html");
        helper.setTo(to);
        helper.setSubject("Registration on WritersNets.com");
        helper.setFrom(mailAddr);
        mailSender.send(mimeMessage);
    }

    private void sendChangePasswordConfirmationEmail(final String to, final String fullName, final String activationToken) throws MessagingException {
        final String host = environment.getProperty("writersnet.addr");
        final String mailAddr = environment.getProperty("writersnet.mail");
        String link = "<a href=\"" + host + "?token=" + activationToken + "&email=" + to + "\" target=\"_blank\">Confirm</a>";
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        String htmlMsg = "<!doctype html>" +
                "<html>" +
                "  <head>" +
                "    <meta name=\"viewport\" content=\"width=device-width\" />" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8" +
                "\" />" +
                "    <title>Registration on WritersNets.com</title>" +
                "    <style>" +
                "      /* -------------------------------------" +
                "          GLOBAL RESETS" +
                "      ------------------------------------- */" +
                "      img {" +
                "        border: none;" +
                "        -ms-interpolation-mode: bicubic;" +
                "        max-width: 100%; }" +
                "" +
                "      body {" +
                "        background-color: #f6f6f6;" +
                "        font-family: sans-serif;" +
                "        -webkit-font-smoothing: antialiased;" +
                "        font-size: 14px;" +
                "        line-height: 1.4;" +
                "        margin: 0;" +
                "        padding: 0;" +
                "        -ms-text-size-adjust: 100%;" +
                "        -webkit-text-size-adjust: 100%; }" +
                "" +
                "      table {" +
                "        border-collapse: separate;" +
                "        mso-table-lspace: 0pt;" +
                "        mso-table-rspace: 0pt;" +
                "        width: 100%; }" +
                "        table td {" +
                "          font-family: sans-serif;" +
                "          font-size: 14px;" +
                "          vertical-align: top; }" +
                "" +
                "      /* -------------------------------------" +
                "          BODY & CONTAINER" +
                "      ------------------------------------- */" +
                "" +
                "      .body {" +
                "        background-color: #f6f6f6;" +
                "        width: 100%; }" +
                "      .container {" +
                "        display: block;" +
                "        Margin: 0 auto !important;" +
                "        /* makes it centered */" +
                "        max-width: 580px;" +
                "        padding: 10px;" +
                "        width: 580px; }" +
                "      .content {" +
                "        box-sizing: border-box;" +
                "        display: block;" +
                "        Margin: 0 auto;" +
                "        max-width: 580px;" +
                "        padding: 10px; }" +
                "" +
                "      /* -------------------------------------" +
                "          HEADER, FOOTER, MAIN" +
                "      ------------------------------------- */" +
                "      .main {" +
                "        background: #ffffff;" +
                "        border-radius: 3px;" +
                "        width: 100%; }" +
                "" +
                "      .wrapper {" +
                "        box-sizing: border-box;" +
                "        padding: 20px; }" +
                "" +
                "      .content-block {" +
                "        padding-bottom: 10px;" +
                "        padding-top: 10px;" +
                "      }" +
                "" +
                "      .footer {" +
                "        clear: both;" +
                "        Margin-top: 10px;" +
                "        text-align: center;" +
                "        width: 100%; }" +
                "        .footer td," +
                "        .footer p," +
                "        .footer span," +
                "        .footer a {" +
                "          color: #999999;" +
                "          font-size: 12px;" +
                "          text-align: center; }" +
                "" +
                "      /* -------------------------------------" +
                "          TYPOGRAPHY" +
                "      ------------------------------------- */" +
                "      h1," +
                "      h2," +
                "      h3," +
                "      h4 {" +
                "        color: #000000;" +
                "        font-family: sans-serif;" +
                "        font-weight: 400;" +
                "        line-height: 1.4;" +
                "        margin: 0;" +
                "        Margin-bottom: 30px; }" +
                "" +
                "      h1 {" +
                "        font-size: 35px;" +
                "        font-weight: 300;" +
                "        text-align: center;" +
                "        text-transform: capitalize; }" +
                "" +
                "      p," +
                "      ul," +
                "      ol {" +
                "        font-family: sans-serif;" +
                "        font-size: 14px;" +
                "        font-weight: normal;" +
                "        margin: 0;" +
                "        Margin-bottom: 15px; }" +
                "        p li," +
                "        ul li," +
                "        ol li {" +
                "          list-style-position: inside;" +
                "          margin-left: 5px; }" +
                "" +
                "      a {" +
                "        color: #3498db;" +
                "        text-decoration: underline; }" +
                "" +
                "      /* -------------------------------------" +
                "          BUTTONS" +
                "      ------------------------------------- */" +
                "      .btn {" +
                "        box-sizing: border-box;" +
                "        width: 100%; }" +
                "        .btn > tbody > tr > td {" +
                "          padding-bottom: 15px; }" +
                "        .btn table {" +
                "          width: auto; }" +
                "        .btn table td {" +
                "          background-color: #ffffff;" +
                "          border-radius: 5px;" +
                "          text-align: center; }" +
                "        .btn a {" +
                "          background-color: #ffffff;" +
                "          border: solid 1px #3498db;" +
                "          border-radius: 5px;" +
                "          box-sizing: border-box;" +
                "          color: #3498db;" +
                "          cursor: pointer;" +
                "          display: inline-block;" +
                "          font-size: 14px;" +
                "          font-weight: bold;" +
                "          margin: 0;" +
                "          padding: 12px 25px;" +
                "          text-decoration: none;" +
                "          text-transform: capitalize; }" +
                "" +
                "      .btn-primary table td {" +
                "        background-color: #3498db; }" +
                "" +
                "      .btn-primary a {" +
                "        background-color: #3498db;" +
                "        border-color: #3498db;" +
                "        color: #ffffff; }" +
                "" +
                "      /* -------------------------------------" +
                "          OTHER STYLES THAT MIGHT BE USEFUL" +
                "      ------------------------------------- */" +
                "      .last {" +
                "        margin-bottom: 0; }" +
                "" +
                "      .first {" +
                "        margin-top: 0; }" +
                "" +
                "      .align-center {" +
                "        text-align: center; }" +
                "" +
                "      .align-right {" +
                "        text-align: right; }" +
                "" +
                "      .align-left {" +
                "        text-align: left; }" +
                "" +
                "      .clear {" +
                "        clear: both; }" +
                "" +
                "      .mt0 {" +
                "        margin-top: 0; }" +
                "" +
                "      .mb0 {" +
                "        margin-bottom: 0; }" +
                "" +
                "      .preheader {" +
                "        color: transparent;" +
                "        display: none;" +
                "        height: 0;" +
                "        max-height: 0;" +
                "        max-width: 0;" +
                "        opacity: 0;" +
                "        overflow: hidden;" +
                "        mso-hide: all;" +
                "        visibility: hidden;" +
                "        width: 0; }" +
                "" +
                "      .powered-by a {" +
                "        text-decoration: none; }" +
                "" +
                "      hr {" +
                "        border: 0;" +
                "        border-bottom: 1px solid #f6f6f6;" +
                "        Margin: 20px 0; }" +
                "" +
                "      /* -------------------------------------" +
                "          RESPONSIVE AND MOBILE FRIENDLY STYLES" +
                "      ------------------------------------- */" +
                "      @media only screen and (max-width: 620px) {" +
                "        table[class=body] h1 {" +
                "          font-size: 28px !important;" +
                "          margin-bottom: 10px !important; }" +
                "        table[class=body] p," +
                "        table[class=body] ul," +
                "        table[class=body] ol," +
                "        table[class=body] td," +
                "        table[class=body] span," +
                "        table[class=body] a {" +
                "          font-size: 16px !important; }" +
                "        table[class=body] .wrapper," +
                "        table[class=body] .article {" +
                "          padding: 10px !important; }" +
                "        table[class=body] .content {" +
                "          padding: 0 !important; }" +
                "        table[class=body] .container {" +
                "          padding: 0 !important;" +
                "          width: 100% !important; }" +
                "        table[class=body] .main {" +
                "          border-left-width: 0 !important;" +
                "          border-radius: 0 !important;" +
                "          border-right-width: 0 !important; }" +
                "        table[class=body] .btn table {" +
                "          width: 100% !important; }" +
                "        table[class=body] .btn a {" +
                "          width: 100% !important; }" +
                "        table[class=body] .img-responsive {" +
                "          height: auto !important;" +
                "          max-width: 100% !important;" +
                "          width: auto !important; }}" +
                "" +
                "      /* -------------------------------------" +
                "          PRESERVE THESE STYLES IN THE HEAD" +
                "      ------------------------------------- */" +
                "      @media all {" +
                "        .ExternalClass {" +
                "          width: 100%; }" +
                "        .ExternalClass," +
                "        .ExternalClass p," +
                "        .ExternalClass span," +
                "        .ExternalClass font," +
                "        .ExternalClass td," +
                "        .ExternalClass div {" +
                "          line-height: 100%; }" +
                "        .apple-link a {" +
                "          color: inherit !important;" +
                "          font-family: inherit !important;" +
                "          font-size: inherit !important;" +
                "          font-weight: inherit !important;" +
                "          line-height: inherit !important;" +
                "          text-decoration: none !important; }" +
                "        .btn-primary table td:hover {" +
                "          background-color: #34495e !important; }" +
                "        .btn-primary a:hover {" +
                "          background-color: #34495e !important;" +
                "          border-color: #34495e !important; } }" +
                "" +
                "    </style>" +
                "  </head>" +
                "  <body class=\"\">" +
                "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"body\">" +
                "      <tr>" +
                "        <td>&nbsp;</td>" +
                "        <td class=\"container\">" +
                "          <div class=\"content\">" +
                "" +
                "            <!-- START CENTERED WHITE CONTAINER -->" +
                "            <span class=\"preheader\">Registration on WritersNets.com</span" +
                ">" +
                "            <table class=\"main\">" +
                "" +
                "              <!-- START MAIN CONTENT AREA -->" +
                "              <tr>" +
                "                <td class=\"wrapper\">" +
                "                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">" +
                "                    <tr>" +
                "                      <td>" +
                "                        <p>Hello " + fullName + ",</p>" +
                "                        <p>Seems you've forgot your password and asked us to change it. </p>" +
                "                        <p>If you didn't do this then simply ignore this message.</p>" +
                "                        <p>If you confirm you want to change your password please click on the following button:</p>" +
                "                        <table border=\"0\" cellpadding=\"0\" cellspacing" +
                "=\"0\" class=\"btn btn-primary\">" +
                "                          <tbody>" +
                "                            <tr>" +
                "                              <td align=\"center\">" +
                "                                <table border=\"0\" cellpadding=\"0\" cells" +
                "pacing=\"0\">" +
                "                                  <tbody>" +
                "                                    <tr>" +
                "                                      <td>" + link + "</td>" +
                "                                    </tr>" +
                "                                  </tbody>" +
                "                                </table>" +
                "                              </td>" +
                "                            </tr>" +
                "                          </tbody>" +
                "                        </table>" +
                "                      </td>" +
                "                    </tr>" +
                "                  </table>" +
                "                </td>" +
                "              </tr>" +
                "" +
                "            <!-- END MAIN CONTENT AREA -->" +
                "            </table>" +
                "" +
                "            <!-- START FOOTER -->" +
                "            <div class=\"footer\">" +
                "              <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">" +
                "                <tr>" +
                "                  <td class=\"content-block powered-by\">" +
                "                    (c) 2018, <a href=\"https://writersnets.com\">WritersNets.com</a>" +
                "                  </td>" +
                "                </tr>" +
                "              </table>" +
                "            </div>" +
                "            <!-- END FOOTER -->" +
                "" +
                "          <!-- END CENTERED WHITE CONTAINER -->" +
                "          </div>" +
                "        </td>" +
                "        <td>&nbsp;</td>" +
                "      </tr>" +
                "    </table>" +
                "  </body>" +
                "</html>";
        mimeMessage.setHeader("Content-Type", "text/html; charset=UTF-8");
        mimeMessage.setContent(htmlMsg, "text/html");
        helper.setTo(to);
        helper.setSubject("Change password confirmation");
        helper.setFrom(mailAddr);
        mailSender.send(mimeMessage);
    }
}