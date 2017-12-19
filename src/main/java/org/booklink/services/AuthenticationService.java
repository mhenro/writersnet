package org.booklink.services;

import org.booklink.models.entities.Section;
import org.booklink.models.entities.User;
import org.booklink.models.request_models.Credentials;
import org.booklink.repositories.SectionRepository;
import org.booklink.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

import static org.booklink.utils.SecurityHelper.generateActivationToken;

/**
 * Created by mhenr on 14.11.2017.
 */
@Service
public class AuthenticationService {
    private UserRepository userRepository;
    private SectionRepository sectionRepository;
    private JavaMailSender mailSender;

    @Autowired
    public AuthenticationService(final UserRepository userRepository, final SectionRepository sectionRepository, final JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.sectionRepository = sectionRepository;
        this.mailSender = mailSender;
    }

    public String auth(final Credentials credentials) {
        String result = null;
        User user = userRepository.findOne(credentials.getUsername());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (user != null && passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
            result = generateActivationToken(user);
        }
        return result;
    }

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

    public boolean register(final Credentials credentials) {
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
        user.setAuthority("ROLE_USER");

        createRegistrationLink(user);

        return true;
    }

    private void createRegistrationLink(final User user) {
        final String token = generateActivationToken(user);
        user.setActivationToken(token);
        userRepository.save(user);

        final Section defaultSection = createDefaultSection(user);
        sectionRepository.save(defaultSection);

        sendRegistrationEmail(user.getEmail(), token);
    }

    private Section createDefaultSection(User user) {
        Section section = new Section();
        section.setAuthor(user);
        section.setLastUpdated(new Date());
        section.setName("Author section");
        return section;
    }

    private void sendRegistrationEmail(String to, String activationToken) {
        String link = "<a href=\"http://booklink.org/activate?activationToken=" + activationToken + "\">Click here to complete the registration!</a>";

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("bot@booklink.org");
        msg.setTo(to);
        msg.setSubject("Registration on booklink.org");
        msg.setText("Congratulations!\n" +
                "You've almost finished the registration!\n" +
                "The final step is to go to the following link: \n" +
                link);
        mailSender.send(msg);
    }
}