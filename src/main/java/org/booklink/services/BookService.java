package org.booklink.services;

import liquibase.util.file.FilenameUtils;
import org.booklink.models.entities.*;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.top_models.*;
import org.booklink.models.request_models.BookTextRequest;
import org.booklink.models.request_models.CoverRequest;
import org.booklink.repositories.*;
import org.booklink.services.convertors.BookConvertor;
import org.booklink.services.convertors.DocToHtmlConvertor;
import org.booklink.services.convertors.PdfToHtmlConvertor;
import org.booklink.services.convertors.TextToHtmlConvertor;
import org.booklink.utils.ObjectHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

/**
 * Created by mhenr on 15.11.2017.
 */
@Service
public class BookService {
    private Environment env;
    private BookRepository bookRepository;
    private BookCommentsRepository bookCommentsRepository;
    private BookTextRepository bookTextRepository;
    private SerieRepository serieRepository;
    private AuthorRepository authorRepository;

    @Autowired
    public BookService(final Environment env,
                       final BookRepository bookRepository,
                       final BookCommentsRepository bookCommentsRepository,
                       final BookTextRepository bookTextRepository,
                       final SerieRepository serieRepository,
                       final AuthorRepository authorRepository) {
        this.env = env;
        this.bookRepository = bookRepository;
        this.bookCommentsRepository = bookCommentsRepository;
        this.bookTextRepository = bookTextRepository;
        this.serieRepository = serieRepository;
        this.authorRepository = authorRepository;
    }

    public Page<Book> getBooks(final Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable);
        if (books != null) {
            processBooks(books);
        }
        return books;
    }

    public Page<TopBookNovelties> getBooksByLastUpdate(final Pageable pageable) {
        Page<TopBookNovelties> books = bookRepository.findAllByLastUpdate(pageable);
        return books;
    }

    public Page<TopBookRating> getBooksByRating(final Pageable pageable) {
        Page<TopBookRating> books = bookRepository.findAllByRating(pageable);
        return books;
    }

    public Page<TopBookVolume> getBooksByVolume(final Pageable pageable) {
        Page<TopBookVolume> books = bookRepository.findAllByVolume(pageable);
        return books;
    }

    public Page<TopBookComments> getBooksByComments(final Pageable pageable) {
        Page<TopBookComments> books = bookRepository.findAllByComments(pageable);
        return books;
    }

    public Page<TopBookViews> getBooksByViews(final Pageable pageable) {
        Page<TopBookViews> books = bookRepository.findAllByViews(pageable);
        return books;
    }

    public Book getBook(final Long bookId) {
        final Book book = bookRepository.findOne(bookId);
        if (book != null) {
            increaseBookViews(book);
            hideAuthInfo(book);
            calcBookSize(book);
            removeRecursionFromBook(book);
        }
        return book;
    }

    public Long saveBook(final Book book) {
        Book savedBook;
        if (book.getId() == null) { //new book
            checkCredentials(book.getAuthorName());   //only owner can edit his book
            savedBook = new Book();
            savedBook.setCreated(new Date());
            savedBook.setLastUpdate(new Date());
        } else {    //saved book was edited
            savedBook = bookRepository.findOne(book.getId());
            if (savedBook == null) {
                throw new ObjectNotFoundException("Book was not found");
            }
            checkCredentials(savedBook.getAuthor().getUsername());   //only owner can edit his book
            savedBook.setLastUpdate(new Date());
        }
        BeanUtils.copyProperties(book, savedBook, ObjectHelper.getNullPropertyNames(book));
        if (book.getSerieId() != null) {
            BookSerie bookSerie = serieRepository.findOne(book.getSerieId());
            if (bookSerie != null) {
                savedBook.setBookSerie(bookSerie);
            }
        } else {
            savedBook.setBookSerie(null);
        }
        if (book.getAuthorName() != null) {
            User user = updateDateInUserSection(book.getAuthorName());
            if (user != null) {
                savedBook.setAuthor(user);
            }
        }
        bookRepository.save(savedBook);

        return savedBook.getId();
    }

    public void saveCover(final CoverRequest coverRequest) throws IOException {
        checkCredentials(coverRequest.getUserId()); //only the owner can change the cover of his book

        Book book = bookRepository.findOne(coverRequest.getId());
        if (book == null) {
            throw new ObjectNotFoundException();
        }
        checkCredentials(book.getAuthor().getUsername()); //only the owner can change the cover of his book
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
    }

    public Date saveBookText(final BookTextRequest bookTextRequest) throws Exception {
        checkCredentials(bookTextRequest.getUserId()); //only the owner can change the cover of his book

        Book book = bookRepository.findOne(bookTextRequest.getBookId());
        if (book == null) {
            throw new ObjectNotFoundException("Book was not found");
        }
        checkCredentials(book.getAuthor().getUsername()); //only the owner can change the text of his book
        String text = convertBookTextToHtml(bookTextRequest);
        BookText bookText = Optional.ofNullable(book.getBookText()).map(txt -> bookTextRepository.findOne(txt.getId())).orElseGet(BookText::new);
        bookText.setText(text);
        bookTextRepository.save(bookText);
        book.setBookText(bookText);
        book.setLastUpdate(new Date());
        bookRepository.save(book);

        return book.getLastUpdate();
    }

    public void deleteBook(final Long bookId) {
        Book book = bookRepository.findOne(bookId);
        if (book == null) {
            throw new ObjectNotFoundException("Book was not found");
        }
        checkCredentials(book.getAuthor().getUsername());   //only owner can delete his book
        removeAllComments(book);
        bookRepository.delete(bookId);
        updateDateInUserSection(book.getAuthor().getUsername());
    }

    private void increaseBookViews(final Book book) {
        if (book != null) {
            final long views = book.getViews() + 1;
            book.setViews(views);
            bookRepository.save(book);
        }
    }

    private void removeAllComments(final Book book) {
        Iterable<BookComments> comments = bookCommentsRepository.findAllByBookId(book.getId());
        bookCommentsRepository.delete(comments);
    }

    private User updateDateInUserSection(final String userId) {
        User user = authorRepository.findOne(userId);
        if (user != null) {
            user.getSection().setLastUpdated(new Date());
            authorRepository.save(user);
        }
        return user;
    }

    private void processBooks(final Page<Book> books) {
        final String defaultCover = env.getProperty("writersnet.coverwebstorage.path") + "\\default_cover.png";
        books.forEach(book -> {
            hideAuthInfo(book);
            calcBookSize(book);
            hideText(book);
            removeRecursionFromBook(book);
            if (book.getCover() == null || book.getCover().isEmpty()) {
                book.setCover(defaultCover);
            }
        });
    }

    private void hideAuthInfo(Book book) {
        User user = book.getAuthor();
        user.setPassword("");
        user.setActivationToken("");
        user.setAuthority("");
    }

    private void removeRecursionFromBook(Book book) {
        User user = book.getAuthor();
        user.setBooks(null);
        user.setSection(null);
        user.setSubscriptions(null);
        user.setSubscribers(null);
    }

    private void calcBookSize(Book book) {
        int size = Optional.ofNullable(book.getBookText()).map(bookText -> bookText.getText().length()).orElse(0);
        book.setSize(size);
    }

    private void hideText(Book book) {
        book.setBookText(null);
    }

    private void checkCredentials(final String userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        if (!currentUser.equals(userId)) {
            throw new UnauthorizedUserException();
        }
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
}
