package org.booklink.controllers;

import org.booklink.models.Response;
import org.booklink.models.entities.User;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.repositories.AuthorRepository;
import org.booklink.utils.ObjectHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Created by mhenr on 16.10.2017.
 */
@RestController
public class AuthorController {
    private AuthorRepository authorRepository;

    @Autowired
    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @CrossOrigin
    @RequestMapping(value = "authors", method = RequestMethod.GET)
    public Page<User> getAuthors(Pageable pageable) {
        Page<User> authors = authorRepository.findAllEnabled(pageable);
        authors.forEach(author -> {
            hideAuthInfo(author);
            removeRecursionFromAuthor(author);
        });
        return authors;
    }

    @CrossOrigin
    @RequestMapping(value = "authors/{authorId:.+}", method = RequestMethod.GET)
    public ResponseEntity<?> getAuthor(@PathVariable String authorId) {
        User user = authorRepository.findOne(authorId);
        if (user != null) {
            hideAuthInfo(user);
            removeRecursionFromAuthor(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        Response<String> response = new Response<>();
        response.setCode(1);
        response.setMessage("Author not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "authors", method = RequestMethod.POST)
    public ResponseEntity<?> saveAuthor(@RequestBody User author) {
        /* checking credentials */
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        if (!currentUser.equals(author.getUsername())) {
            throw new UnauthorizedUserException();
        }

        try {
            User user = authorRepository.findOne(author.getUsername());
            if (user == null) {
                throw new ObjectNotFoundException();
            }
            BeanUtils.copyProperties(author, user, ObjectHelper.getNullPropertyNames(author));
            if (author.getSectionName() != null) {
                user.getSection().setName(author.getSectionName());
            }
            if (author.getSectionDescription() != null) {
                user.getSection().setDescription(author.getSectionDescription());
            }
            authorRepository.save(user);
        } catch(Exception e) {
            Response<String> response = new Response<>();
            response.setCode(1);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Response<String> response = new Response<>();
        response.setCode(0);
        response.setMessage("Author was saved");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void hideAuthInfo(User user) {
        user.setPassword("");
        user.setActivationToken("");
        user.setAuthority("");
    }

    private void removeRecursionFromAuthor(User user){
        user.getBooks().stream().forEach(book -> book.setAuthor(null));
        if (user.getSection() != null) {
            user.getSection().setAuthor(null);
        }
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<?> unauthorizedUser(UnauthorizedUserException e) {
        Response<String> response = new Response<>();
        response.setCode(1);
        response.setMessage("Bad credentials");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> userNotFount(ObjectNotFoundException e) {
        Response<String> response = new Response<>();
        response.setCode(5);
        response.setMessage("User not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
