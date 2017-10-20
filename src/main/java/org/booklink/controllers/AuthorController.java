package org.booklink.controllers;

import org.booklink.models.Response;
import org.booklink.models.entities.User;
import org.booklink.repositories.AuthorRepository;
import org.booklink.repositories.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
