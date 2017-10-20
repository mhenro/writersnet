package org.booklink.controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.booklink.models.*;
import org.booklink.models.entities.Section;
import org.booklink.models.entities.User;
import org.booklink.models.exceptions.ObjectAlreadyExistException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.repositories.SectionRepository;
import org.booklink.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by mhenr on 02.10.2017.
 */
@RestController
@RequestMapping("/")
public class AuthenticationController {
    private UserRepository userRepository;
    private SectionRepository sectionRepository;
    private JavaMailSender mailSender;

    @Autowired
    public AuthenticationController(UserRepository userRepository, SectionRepository sectionRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.sectionRepository = sectionRepository;
        this.mailSender = mailSender;
    }

    @CrossOrigin
    @RequestMapping(value = "auth", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Response<String>> auth(@RequestBody Credentials credentials) {
        User user = userRepository.findOne(credentials.getUsername());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (user != null && passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
            String token = generateActivationToken(user);
            Response<String> response = new Response<>();
            response.setCode(0);
            response.setMessage(token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new UnauthorizedUserException();
        }
    }

    @CrossOrigin
    @RequestMapping(value = "register", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Response<String>> register(@RequestBody Credentials credentials) {
        User user = userRepository.findOne(credentials.getUsername());
        if (user != null) {
            throw new ObjectAlreadyExistException();
        }
        user = userRepository.findUserByEmail(credentials.getEmail());
        if (user != null) {
            throw new ObjectAlreadyExistException();
        }
        final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        final String password = passwordEncoder.encode(credentials.getPassword());
        user = new User();
        user.setEmail(credentials.getEmail());
        user.setUsername(credentials.getUsername());
        user.setPassword(password);
        user.setEnabled(false);
        user.setAuthority("ROLE_USER");

        /* creating registration link */
        final String activationToken = generateActivationToken(user);
        user.setActivationToken(activationToken);
        userRepository.save(user);
        Section defaultSection = createDefaultSection(user);
        sectionRepository.save(defaultSection);

        sendRegistrationEmail(user.getEmail(), activationToken);  //username = email

        Response<String> response = new Response<>();
        response.setCode(0);
        response.setMessage("OK");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private Section createDefaultSection(User user) {
        Section section = new Section();
        section.setAuthor(user);
        section.setLastUpdated(new Date());
        section.setName("Author section");
        section.setVisitors(0);
        return section;
    }

    @CrossOrigin
    @RequestMapping(value = "activate", method = RequestMethod.GET)
    public ResponseEntity<Response<String>> activate(@RequestParam("activationToken") String activationToken) {
        Response<String> response = new Response<>();
        User user = userRepository.findUserByActivationToken(activationToken);
        if (user != null) {
            user.setEnabled(true);
            user.setActivationToken("");
            userRepository.save(user);

            response.setCode(0);
            response.setMessage("User activation was completed! Please log-in.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response.setCode(3);
        response.setMessage("Activation user error");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    private String generateActivationToken(User user) {
        String result = Jwts.builder().setSubject(user.getUsername()).claim("roles", user.getAuthority()).claim("enabled", user.getEnabled()).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis()+15*60*1000)).signWith(SignatureAlgorithm.HS256, "booklink").compact();;

        return result;
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








    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<Response<String>> unauthorizedUser(UnauthorizedUserException e) {
        Response<String> response = new Response<>();
        response.setCode(1);
        response.setMessage("Bad credentials");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ObjectAlreadyExistException.class)
    public ResponseEntity<Response<String>> objectAlreadyExist(ObjectAlreadyExistException e) {
        Response<String> response = new Response<>();
        response.setCode(2);
        response.setMessage("Object already exist");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
