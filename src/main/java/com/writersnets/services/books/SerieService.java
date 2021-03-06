package com.writersnets.services.books;

import com.writersnets.models.entities.books.BookSerie;
import com.writersnets.models.exceptions.ObjectNotFoundException;
import com.writersnets.models.request.SerieRequest;
import com.writersnets.models.response.BookSerieResponse;
import com.writersnets.repositories.SerieRepository;
import com.writersnets.services.security.AuthorizedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            bookSerie = serieRepository.findById(serie.getId()).orElseThrow(() -> new ObjectNotFoundException("Serie is not found"));
            bookSerie.setName(serie.getName());
        }
        serieRepository.save(bookSerie);

        return bookSerie.getId();
    }

    @Transactional
    public void deleteSerie(final Long serieId) {
        checkCredentials();   //only owner can delete his series
        BookSerie bookSerie = serieRepository.findById(serieId).orElseThrow(() -> new ObjectNotFoundException("Serie is not found"));
        serieRepository.delete(bookSerie);
    }

    private void checkCredentials() {
        authorizedUserService.getAuthorizedUser();
    }
}
