package org.booklink.controllers;

import liquibase.util.file.FilenameUtils;
import org.booklink.models.request_models.AvatarRequest;
import org.booklink.models.request_models.BookTextRequest;
import org.booklink.models.request_models.CoverRequest;
import org.booklink.models.Response;
import org.booklink.models.entities.Book;
import org.booklink.models.entities.BookText;
import org.booklink.models.entities.User;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.repositories.AuthorRepository;
import org.booklink.repositories.BookRepository;
import org.booklink.repositories.BookTextRepository;
import org.booklink.services.convertors.BookConvertor;
import org.booklink.services.convertors.DocToHtmlConvertor;
import org.booklink.services.convertors.PdfToHtmlConvertor;
import org.booklink.services.convertors.TextToHtmlConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.Optional;

/**
 * Created by mhenr on 27.10.2017.
 */
@RestController
public class FileController {
    private Environment env;
    private AuthorRepository authorRepository;
    private BookRepository bookRepository;
    private BookTextRepository bookTextRepository;

    @Autowired
    public FileController(Environment env, AuthorRepository authorRepository, BookRepository bookRepository, BookTextRepository bookTextRepository) {
        this.env = env;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.bookTextRepository = bookTextRepository;
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
            checkAuthority(author.getUsername()); //only the owner can change his avatar
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
            checkAuthority(book.getAuthor().getUsername()); //only the owner can change the cover of his book
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
            book.setLastUpdate(new Date());
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

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "text", method = RequestMethod.POST)
    public ResponseEntity<?> saveBookText(BookTextRequest bookTextRequest) {
        checkAuthority(bookTextRequest.getUserId());

        Response<String> response = new Response<>();
        try {
            Book book = bookRepository.findOne(bookTextRequest.getBookId());
            if (book == null) {
                throw new ObjectNotFoundException();
            }
            checkAuthority(book.getAuthor().getUsername()); //only the owner can change the text of his book
            String text = convertBookTextToHtml(bookTextRequest);
            BookText bookText = Optional.ofNullable(book.getBookText()).map(txt -> bookTextRepository.findOne(txt.getId())).orElseGet(BookText::new);
            bookText.setText(text);
            bookTextRepository.save(bookText);
            book.setBookText(bookText);
            book.setLastUpdate(new Date());
            bookRepository.save(book);
        } catch(Exception e) {
            response.setCode(1);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.setCode(0);
        response.setMessage("Book text was saved successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /* convert user file to our internal html format */
    private String convertBookTextToHtml(BookTextRequest bookTextRequest) throws Exception {
        MultipartFile textFile = bookTextRequest.getText();
        String ext = FilenameUtils.getExtension(textFile.getOriginalFilename());
        String path = env.getProperty("writersnet.tempstorage") + bookTextRequest.getUserId() + "\\";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        String result = "";
        BookConvertor<String> textBookConvertor = new TextToHtmlConvertor();
        switch (ext) {
            case "txt":
                String text = new String(textFile.getBytes(), "UTF-8");
                result = textBookConvertor.toHtml(text);
                break;
            case "docx":
                File tmpDoc = new File(path + textFile.getOriginalFilename());
                textFile.transferTo(tmpDoc);
                BookConvertor<File> docConvertor = new DocToHtmlConvertor();
                result = textBookConvertor.toHtml(docConvertor.toHtml(tmpDoc));
                tmpDoc.delete();
                break;
            case "pdf":
                File tmpPdf = new File(path + textFile.getOriginalFilename());
                textFile.transferTo(tmpPdf);
                BookConvertor<File> pdfBookConvertor = new PdfToHtmlConvertor();
                result = textBookConvertor.toHtml(pdfBookConvertor.toHtml(tmpPdf));
                tmpPdf.delete();
                break;
            default: throw new RuntimeException("Unsupported text format");
        }
        return result;
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
