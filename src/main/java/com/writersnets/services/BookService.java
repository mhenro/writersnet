package com.writersnets.services;

import liquibase.util.file.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import com.writersnets.models.Genre;
import com.writersnets.models.entities.*;
import com.writersnets.models.exceptions.IsNotPremiumUserException;
import com.writersnets.models.exceptions.ObjectNotFoundException;
import com.writersnets.models.exceptions.PermissionDeniedException;
import com.writersnets.models.exceptions.UnauthorizedUserException;
import com.writersnets.models.request.BookRequest;
import com.writersnets.models.response.BookCostResponse;
import com.writersnets.models.response.BookResponse;
import com.writersnets.models.response.BookWithTextResponse;
import com.writersnets.models.top_models.*;
import com.writersnets.models.request.BookTextRequest;
import com.writersnets.models.request.CoverRequest;
import com.writersnets.repositories.*;
import com.writersnets.services.convertors.BookConvertor;
import com.writersnets.services.convertors.DocToHtmlConvertor;
import com.writersnets.services.convertors.PdfToHtmlConvertor;
import com.writersnets.services.convertors.TextToHtmlConvertor;
import com.writersnets.utils.ObjectHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xhtmlrenderer.resource.XMLResource;
import org.xml.sax.InputSource;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created by mhenr on 15.11.2017.
 */
@Service
@Transactional(readOnly = true)
public class BookService {
    private Environment env;
    private BookRepository bookRepository;
    private BookTextRepository bookTextRepository;
    private SerieRepository serieRepository;
    private UserBookRepository userBookRepository;
    private AuthorRepository authorRepository;
    private AuthorizedUserService authorizedUserService;
    private NewsService newsService;

    @Autowired
    public BookService(final Environment env,
                       final BookRepository bookRepository,
                       final BookTextRepository bookTextRepository,
                       final SerieRepository serieRepository,
                       final UserBookRepository userBookRepository,
                       final AuthorRepository authorRepository,
                       final AuthorizedUserService authorizedUserService,
                       final NewsService newsService) {
        this.env = env;
        this.bookRepository = bookRepository;
        this.bookTextRepository = bookTextRepository;
        this.serieRepository = serieRepository;
        this.userBookRepository = userBookRepository;
        this.authorRepository = authorRepository;
        this.authorizedUserService = authorizedUserService;
        this.newsService = newsService;
    }

    //TODO: refactor this hell!
    public Page<BookResponse> getBooks(final String genreStr, final String language, final Pageable pageable) {
        Page<BookResponse> books;
        final Genre genre = Optional.ofNullable(genreStr).map(Genre::valueOf).orElse(null);
        if (genre != null && language != null) {
            switch(pageable.getSort().toString().split(":")[0]) {
                case "totalRating": books = bookRepository.getAllBooksSortedByRating(genre, language, pageable); break;
                case "lastUpdate": books = bookRepository.getAllBooksSortedByDate(genre, language, pageable); break;
                default: books = bookRepository.getAllBooksSortedByName(genre, language, pageable);
            }
        } else if (genre != null) {
            switch(pageable.getSort().toString().split(":")[0]) {
                case "totalRating": books = bookRepository.getAllBooksSortedByRating(genre, pageable); break;
                case "lastUpdate": books = bookRepository.getAllBooksSortedByDate(genre, pageable); break;
                default: books = bookRepository.getAllBooksSortedByName(genre, pageable);
            }
        }
        else if (language != null) {
            switch(pageable.getSort().toString().split(":")[0]) {
                case "totalRating": books = bookRepository.getAllBooksSortedByRating(language, pageable); break;
                case "lastUpdate": books = bookRepository.getAllBooksSortedByDate(language, pageable); break;
                default: books = bookRepository.getAllBooksSortedByName(language, pageable);
            }
        } else {
            switch(pageable.getSort().toString().split(":")[0]) {
                case "totalRating": books = bookRepository.getAllBooksSortedByRating(pageable); break;
                case "lastUpdate": books = bookRepository.getAllBooksSortedByDate(pageable); break;
                default: books = bookRepository.getAllBooksSortedByName(pageable);
            }
        }

        books.forEach(this::setDefaultCoverForBookAndUser);
        return books;
    }

    //TODO: refactor this hell!
    public Page<BookResponse> getBooksByName(final String bookName, final String genreStr, final String language, final Pageable pageable) {
        Page<BookResponse> books;
        final Genre genre = Optional.ofNullable(genreStr).map(Genre::valueOf).orElse(null);
        if (genre != null && language != null) {
            switch(pageable.getSort().toString().split(":")[0]) {
                case "totalRating": books = bookRepository.findBooksByRating(bookName, genre, language, pageable); break;
                case "lastUpdate": books = bookRepository.findBooksByDate(bookName, genre, language, pageable); break;
                default: books = bookRepository.findBooksByName(bookName, genre, language, pageable);
            }
        } else if (genre != null) {
            switch(pageable.getSort().toString().split(":")[0]) {
                case "totalRating": books = bookRepository.findBooksByRating(bookName, genre, pageable); break;
                case "lastUpdate": books = bookRepository.findBooksByDate(bookName, genre, pageable); break;
                default: books = bookRepository.findBooksByName(bookName, genre, pageable);
            }
        } else if (language != null) {
            switch(pageable.getSort().toString().split(":")[0]) {
                case "totalRating": books = bookRepository.findBooksByRating(bookName, language, pageable); break;
                case "lastUpdate": books = bookRepository.findBooksByDate(bookName, language, pageable); break;
                default: books = bookRepository.findBooksByName(bookName, language, pageable);
            }
        } else {
            switch(pageable.getSort().toString().split(":")[0]) {
                case "totalRating": books = bookRepository.findBooksByRating(bookName, pageable); break;
                case "lastUpdate": books = bookRepository.findBooksByDate(bookName, pageable); break;
                default: books = bookRepository.findBooksByName(bookName, pageable);
            }
        }
        books.forEach(this::setDefaultCoverForBookAndUser);
        return books;
    }

    public Page<BookResponse> getBooksByAuthor(final String authorId, final Pageable pageable) {
        Page<BookResponse> books = bookRepository.findBooksByAuthor(authorId, pageable);
        books.forEach(this::setDefaultCoverForBookAndUser);
        return books;
    }

    public Long getBooksCount() {
        return bookRepository.getBooksCount();
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

    @Transactional
    public BookWithTextResponse getBook(final Long bookId, final Integer page, final Integer size) {
        BookWithTextResponse pageOfBook;
        if (page != null && size != null) {
            final int startPosition = page * size;
            pageOfBook = bookRepository.getPartBookById(bookId, startPosition, size);
            if (pageOfBook == null) {
                throw new ObjectNotFoundException("Book is not found");
            }
            preparePageOfBook(pageOfBook, page, size);
        } else {
            pageOfBook = bookRepository.getBookById(bookId);    //return full text
            if (pageOfBook == null) {
                throw new ObjectNotFoundException("Book is not found");
            }
        }
        if (pageOfBook.getPaid() == true && !pageOfBook.getCost().equals(0) && !isUserHasBook(bookId)) {
            throw new PermissionDeniedException("This is a paid book, but you didn't buy it yet");
        }
        setDefaultCoverForBook(pageOfBook);
        increaseBookViews(bookId);
        return pageOfBook;
    }

    public BookCostResponse getBookCost(final Long bookId) {
        final BookCostResponse response = bookRepository.getBookCost(bookId);
        if (response == null) {
            throw new ObjectNotFoundException("Book is not found");
        }
        return response;
    }

    @Transactional
    public Long saveBook(final BookRequest book) {
        checkCredentials(book.getAuthorName());   //only owner can edit his book
        Book savedBook;
        if (book.getId() == null) { //new book
            savedBook = new Book();
            savedBook.setCreated(LocalDate.now());
            savedBook.setLastUpdate(LocalDateTime.now());
            final BookText bookText = new BookText();
            savedBook.setBookText(bookText);
        } else {    //saved book was edited
            savedBook = bookRepository.findById(book.getId())
                    .orElseThrow(() -> new ObjectNotFoundException("Book is not found"));
            checkCredentials(savedBook.getAuthor().getUsername());   //only owner can edit his book
            savedBook.setLastUpdate(LocalDateTime.now());
        }
        processCostRequest(book);
        BeanUtils.copyProperties(book, savedBook, ObjectHelper.getNullPropertyNames(book));
        if (book.getSerieId() != null) {
            serieRepository.findById(book.getSerieId())
                    .ifPresent(s -> savedBook.setBookSerie(s));
        } else {
            savedBook.setBookSerie(null);
        }
        User user = null;
        if (book.getAuthorName() != null) {
            user = updateDateInUserSection(book.getAuthorName());
            if (user != null) {
                savedBook.setAuthor(user);
            }
        }
        bookRepository.save(savedBook);
        if (user != null) {
            newsService.createNews(NewsService.NEWS_TYPE.BOOK_UPDATED, user, savedBook);
        }

        return savedBook.getId();
    }

    @Transactional
    public void saveCover(final CoverRequest coverRequest) throws IOException {
        checkCredentials(coverRequest.getUserId()); //only the owner can change the cover of his book

        Book book = bookRepository.findById(coverRequest.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Book is not found"));
        if (coverRequest.getCover().getSize() >= 102400 && !book.getAuthor().getPremium()) {
            throw new IsNotPremiumUserException("Only a premium user can add the book cover larger than 100 Kb");
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
        book.setLastUpdate(LocalDateTime.now());
        bookRepository.save(book);
    }

    @Transactional
    public void restoreDefaultCover(final Long bookId) {
        authorizedUserService.getAuthorizedUser();
        final Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ObjectNotFoundException("Book is not found"));
        book.setCover(null);
        newsService.createNews(NewsService.NEWS_TYPE.BOOK_UPDATED, book.getAuthor(), book);
    }

    @Transactional
    public LocalDateTime saveBookText(final BookTextRequest bookTextRequest) throws IOException {
        checkCredentials(bookTextRequest.getUserId()); //only the owner can change the cover of his book

        Book book = bookRepository.findById(bookTextRequest.getBookId())
                .orElseThrow(() -> new ObjectNotFoundException("Book is not found"));
        if (bookTextRequest.getText().getSize() >= 10485760 && !book.getAuthor().getPremium()) {
            throw new IsNotPremiumUserException("Only a premium user can add the book text larger than 10 Mb");
        }
        checkCredentials(book.getAuthor().getUsername()); //only the owner can change the text of his book
        String text = convertBookTextToHtml(bookTextRequest);
        BookText bookText = Optional.ofNullable(book.getBookText()).map(txt -> bookTextRepository.findById(txt.getId()).orElseGet(BookText::new)).orElseGet(BookText::new);
        bookText.setPrevText(bookText.getText());
        bookText.setText(text);
        bookTextRepository.save(bookText);
        book.setBookText(bookText);
        book.setLastUpdate(LocalDateTime.now());
        bookRepository.save(book);

        return book.getLastUpdate();
    }

    @Transactional
    public void deleteBook(final Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ObjectNotFoundException("Book is not found"));
        checkCredentials(book.getAuthor().getUsername());   //only owner can delete his book
        newsService.createNews(NewsService.NEWS_TYPE.BOOK_DELETED, book.getAuthor(), book);
        bookRepository.deleteById(bookId);
        updateDateInUserSection(book.getAuthor().getUsername());
    }

    public boolean isUserHasBook(final Long bookId) {
        final User user = authorizedUserService.getAuthorizedUser();
        final Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ObjectNotFoundException("Book is not found"));
        if (book.getAuthor().getUsername().equals(user.getUsername())) {
            return true;
        }
        final UserBookPK userBookPK = new UserBookPK();
        userBookPK.setUser(user);
        userBookPK.setBook(book);
        return userBookRepository.findById(userBookPK)
                .map(b -> true)
                .orElse(false);
    }

    private Document getDocument(final String content) {
        String content2 = "Hello world!";
        InputSource is = new InputSource(new BufferedReader(new StringReader(content2)));
        return XMLResource.load(is).getDocument();
    }

    public byte[] getBookAsPdf(final Long bookId) throws IOException {
        final Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ObjectNotFoundException("Book is not found"));
        final BookText bookText = book.getBookText();
/*
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(this.getDocument(bookText.getText()), null);
        renderer.layout();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        renderer.createPDF(baos);
        byte[] bytes = baos.toByteArray();
        baos.close();
        return bytes;*/


        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream stream = new PDPageContentStream(document, page);
        stream.setFont(PDType1Font.HELVETICA, 12);
        stream.beginText();
        stream.showText(new String(bookText.getText().getBytes("UTF-8"), "windows-1251"));
        stream.endText();
        stream.close();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.save(byteArrayOutputStream);
        document.close();

        return byteArrayOutputStream.toByteArray();

    }

    private void preparePageOfBook(final BookWithTextResponse pageOfBook, final int page, final int size) {
        pageOfBook.setTotalPages(pageOfBook.getSize() / size);
        final int offset = page * size;
        final String prefix = offset > 0 ? "..." : "";
        if (offset + size > pageOfBook.getSize()) {
            pageOfBook.getBookText().setText(prefix + pageOfBook.getBookText().getText());
            pageOfBook.getBookText().setPrevText(prefix + pageOfBook.getBookText().getPrevText());
        } else {
            pageOfBook.getBookText().setText(prefix + pageOfBook.getBookText().getText() + "...");
            pageOfBook.getBookText().setPrevText(prefix + pageOfBook.getBookText().getPrevText() + "...");
        }
    }

    private void increaseBookViews(final Long bookId) {
        bookRepository.findById(bookId).ifPresent(book -> {
            final long views = book.getViews() + 1;
            book.setViews(views);
            bookRepository.save(book);
        });
    }

    private void processCostRequest(final BookRequest book) {
        if (!book.getPaid()) {
            book.setCost(0L);
        }
        if (book.getCost() == null || book.getCost().equals(0L)) {
            book.setPaid(false);
        }
    }

    private User updateDateInUserSection(final String userId) {
        Optional<User> user = authorRepository.findById(userId);
        user.ifPresent(u -> {
            u.getSection().setLastUpdated(LocalDate.now());
            authorRepository.save(u);
        });
        return user.orElseGet(null);
    }

    private void setDefaultCoverForBookAndUser(final BookResponse book) {
        final String defaultCover = env.getProperty("writersnet.coverwebstorage.path") + "default_cover.png";
        final String defaultAvatar = env.getProperty("writersnet.avatarwebstorage.path") + "default_avatar.png";
        if (book.getCover() == null || book.getCover().isEmpty()) {
            book.setCover(defaultCover);
        }
        if (book.getAuthor().getAvatar() == null || book.getAuthor().getAvatar().isEmpty()) {
            book.getAuthor().setAvatar(defaultAvatar);
        }
    }

    private void setDefaultCoverForBook(final BookWithTextResponse book) {
        final String defaultCover = env.getProperty("writersnet.coverwebstorage.path") + "default_cover.png";
        if (book.getCover() == null || book.getCover().isEmpty()) {
            book.setCover(defaultCover);
        }
    }

    private void checkCredentials(final String userId) {
        if (!authorizedUserService.getAuthorizedUser().getUsername().equals(userId)) {
            throw new UnauthorizedUserException();
        }
    }

    /* convert user file to our internal html format */
    private String convertBookTextToHtml(BookTextRequest bookTextRequest) throws IOException {
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
