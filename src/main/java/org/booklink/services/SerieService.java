package org.booklink.services;

import org.booklink.models.entities.BookSerie;
import org.booklink.models.entities.User;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.request.SerieRequest;
import org.booklink.models.response.BookSerieResponse;
import org.booklink.repositories.AuthorRepository;
import org.booklink.repositories.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Created by mhenr on 15.11.2017.
 */
@Service
public class SerieService {
    private SerieRepository serieRepository;
    private AuthorRepository authorRepository;

    @Autowired
    public SerieService(final SerieRepository serieRepository, final AuthorRepository authorRepository) {
        this.serieRepository = serieRepository;
        this.authorRepository = authorRepository;
    }

    public Page<BookSerieResponse> getBookSeries(final String userId, final Pageable pageable) {
        return serieRepository.findAllByUserId(userId, pageable);
    }

    public Long saveSerie(final SerieRequest serie) {
        checkCredentials();   //only owner can edit his series
        BookSerie bookSerie;
        if (serie.getId() == null) {    //adding new serie
            bookSerie = new BookSerie();
            bookSerie.setName(serie.getName());
            bookSerie.setAuthor(getAuthorizedUser());
        } else {    //editing existed serie
            bookSerie = serieRepository.findOne(serie.getId());
            if (bookSerie == null) {
                throw new ObjectNotFoundException("Serie was not found");
            }
            bookSerie.setName(serie.getName());
        }
        serieRepository.save(bookSerie);

        return bookSerie.getId();
    }

    public void deleteSerie(final Long serieId) {
        checkCredentials();   //only owner can delete his series
        BookSerie bookSerie = serieRepository.findOne(serieId);
        if (bookSerie == null) {
            throw new ObjectNotFoundException("Serie was not found");
        }
        serieRepository.delete(bookSerie);
    }

    private void checkCredentials() {
        getAuthorizedUser();
    }

    private User getAuthorizedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        final User user = authorRepository.findOne(currentUser);
        if (user == null) {
            throw new ObjectNotFoundException("Bad credentials");
        }
        return user;
    }
}
