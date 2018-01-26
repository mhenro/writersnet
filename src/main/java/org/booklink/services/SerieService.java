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
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mhenr on 15.11.2017.
 */
@Service
@Transactional(readOnly = true)
public class SerieService {
    private SerieRepository serieRepository;
    private AuthorizedUserService authorizedUserService;

    @Autowired
    public SerieService(final SerieRepository serieRepository, final AuthorizedUserService authorizedUserService) {
        this.serieRepository = serieRepository;
        this.authorizedUserService = authorizedUserService;
    }

    public Page<BookSerieResponse> getBookSeries(final String userId, final Pageable pageable) {
        return serieRepository.findAllByUserId(userId, pageable);
    }

    @Transactional
    public Long saveSerie(final SerieRequest serie) {
        checkCredentials();   //only owner can edit his series
        BookSerie bookSerie;
        if (serie.getId() == null) {    //adding new serie
            bookSerie = new BookSerie();
            bookSerie.setName(serie.getName());
            bookSerie.setAuthor(authorizedUserService.getAuthorizedUser());
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

    @Transactional
    public void deleteSerie(final Long serieId) {
        checkCredentials();   //only owner can delete his series
        BookSerie bookSerie = serieRepository.findOne(serieId);
        if (bookSerie == null) {
            throw new ObjectNotFoundException("Serie was not found");
        }
        serieRepository.delete(bookSerie);
    }

    private void checkCredentials() {
        authorizedUserService.getAuthorizedUser();
    }
}
