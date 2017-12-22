package org.booklink.services;

import org.booklink.models.entities.BookSerie;
import org.booklink.models.entities.User;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.request_models.Serie;
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

    public Page<BookSerie> getBookSeries(final String userId, final Pageable pageable) {
        return serieRepository.findAllByUserId(userId, pageable);
    }

    public Long saveSerie(final Serie serie) {
        checkCredentials(serie.getUserId());   //only owner can edit his series
        final User author = authorRepository.findOne(serie.getUserId());
        if (author == null) {
            throw new ObjectNotFoundException("Author was not found");
        }
        BookSerie bookSerie;
        if (serie.getId() == null) {    //adding new serie
            bookSerie = new BookSerie();
            bookSerie.setName(serie.getName());
            bookSerie.setAuthor(author);
        } else {    //editing existed serie
            bookSerie = serieRepository.findOne(serie.getId());
            if (bookSerie == null) {
                throw new ObjectNotFoundException("Serie was not found");
            }
            checkCredentials(bookSerie.getAuthor().getUsername());   //only owner can edit his series
            bookSerie.setName(serie.getName());
        }
        serieRepository.save(bookSerie);

        return bookSerie.getId();
    }

    public void deleteSerie(final Long serieId) {
        BookSerie bookSerie = serieRepository.findOne(serieId);
        if (bookSerie == null) {
            throw new ObjectNotFoundException("Serie was not found");
        }
        checkCredentials(bookSerie.getAuthor().getUsername());   //only owner can delete his series
        serieRepository.delete(bookSerie);
    }

    private void checkCredentials(final String userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        if (!currentUser.equals(userId)) {
            throw new UnauthorizedUserException();
        }
    }
}
