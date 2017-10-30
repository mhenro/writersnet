package org.booklink.controllers;

import liquibase.util.file.FilenameUtils;
import org.booklink.models.AvatarRequest;
import org.booklink.models.CoverRequest;
import org.booklink.models.Response;
import org.booklink.models.entities.Book;
import org.booklink.models.entities.User;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.repositories.AuthorRepository;
import org.booklink.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * Created by mhenr on 27.10.2017.
 */
@RestController
public class FileController {
    private Environment env;
    private HttpServletRequest request;
    private AuthorRepository authorRepository;
    private BookRepository bookRepository;

    @Autowired
    public FileController(Environment env, HttpServletRequest request, AuthorRepository authorRepository, BookRepository bookRepository) {
        this.env = env;
        this.request = request;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "avatar", method = RequestMethod.POST)
    public ResponseEntity<?> saveAvatar(AvatarRequest avatarRequest) {
        checkAuthority(avatarRequest.getUserId());

        Response<String> response = new Response<>();
        try {
            User author = authorRepository.findOne(avatarRequest.getUserId());
            if (author == null) {
                throw new ObjectNotFoundException();
            }

            String uploadDir = env.getProperty("writersnet.avatarstorage.path");
            File file = new File(uploadDir);
            if (!file.exists()) {
                file.mkdir();
            }
            String originalName = avatarRequest.getUserId().toString() + "." + FilenameUtils.getExtension(avatarRequest.getAvatar().getOriginalFilename());

            String filePath = uploadDir + originalName;
            File dest = new File(filePath);
            avatarRequest.getAvatar().transferTo(dest);

            String avatarLink = env.getProperty("writersnet.avatarwebstorage.path") + originalName;
            author.setAvatar(avatarLink);
            authorRepository.save(author);
        } catch(Exception e) {
            response.setCode(1);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.setCode(0);
        response.setMessage("Avatar was saved successully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "cover", method = RequestMethod.POST)
    public ResponseEntity<?> saveCover(CoverRequest coverRequest) {
        checkAuthority(coverRequest.getUserId());

        Response<String> response = new Response<>();
        try {
            Book book = bookRepository.findOne(coverRequest.getId());
            if (book == null) {
                throw new ObjectNotFoundException();
            }
            String uploadDir = env.getProperty("writersnet.coverstorage.path");
            File file = new File(uploadDir);
            if (!file.exists()) {
                file.mkdir();
            }
            String originalName = coverRequest.getId().toString() + "." + FilenameUtils.getExtension(coverRequest.getCover().getOriginalFilename());

            String filePath = uploadDir + originalName;
            File dest = new File(filePath);
            coverRequest.getCover().transferTo(dest);

            String coverLink = env.getProperty("writersnet.coverwebstorage.path") + originalName;
            book.setCover(coverLink);
            bookRepository.save(book);
        } catch(Exception e) {
            response.setCode(1);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.setCode(0);
        response.setMessage("Cover was saved successully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /* method for checking credentials */
    private void checkAuthority(final String userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        if (!currentUser.equals(userId)) {
            throw new UnauthorizedUserException();
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
