package org.booklink.services;

import org.booklink.models.entities.Book;
import org.booklink.models.entities.BookComments;
import org.booklink.models.entities.BookSerie;
import org.booklink.models.entities.User;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.repositories.AuthorRepository;
import org.booklink.repositories.BookCommentsRepository;
import org.booklink.repositories.BookRepository;
import org.booklink.repositories.SerieRepository;
import org.booklink.utils.ObjectHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
    private SerieRepository serieRepository;
    private AuthorRepository authorRepository;

    @Autowired
    public BookService(final Environment env,
                       final BookRepository bookRepository,
                       final BookCommentsRepository bookCommentsRepository,
                       final SerieRepository serieRepository,
                       final AuthorRepository authorRepository) {
        this.env = env;
        this.bookRepository = bookRepository;
        this.bookCommentsRepository = bookCommentsRepository;
        this.serieRepository = serieRepository;
        this.authorRepository = authorRepository;
    }

    public Page<Book> getBooks(Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable);
        processBooks(books);
        return books;
    }

    public Book getBook(final Long bookId) {
        Book book = bookRepository.findOne(bookId);
        if (book != null) {
            hideAuthInfo(book);
            calcBookSize(book);
            //hideText(book);
            removeRecursionFromBook(book);
        }
        return book;
    }

    public Long saveBook(Book book) {
        Book savedBook;
        if (book.getId() == null) { //new book
            savedBook = new Book();
            savedBook.setCreated(new Date());
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
}
