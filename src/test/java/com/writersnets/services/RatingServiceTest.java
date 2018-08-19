package com.writersnets.services;

import com.writersnets.models.entities.books.Rating;
import com.writersnets.models.exceptions.ObjectAlreadyExistException;
import com.writersnets.repositories.BookRepository;
import com.writersnets.repositories.RatingRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by mhenr on 23.11.2017.
 */
@RunWith(SpringRunner.class)
public class RatingServiceTest {
    @TestConfiguration
    static class AuthenticationServiceConfiguration {
        @MockBean
        RatingRepository ratingRepository;
        @MockBean
        BookRepository bookRepository;

        @Bean
        public RatingService ratingService() {
            return new RatingService(ratingRepository, bookRepository);
        }
    }

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RatingService ratingService;

    private HttpServletRequest request;

    @Before
    public void init() {
        request = Mockito.mock(HttpServletRequest.class);
        final Rating rating = new Rating();
        Mockito.when(ratingRepository.findRatingByBookIdAndClientIp(44L, "127.0.0.1")).thenReturn(rating);
        Mockito.when(request.getHeader("X-Real-IP")).thenReturn("172.27.0.20");
    }

    @Test(expected = Test.None.class)
    public void addStar() throws Exception {
        ratingService.addStar(4L, 5, request);
    }

    @Test(expected = ObjectAlreadyExistException.class)
    public void addStar_alreadyExist() throws Exception {
        Mockito.when(request.getHeader("X-Real-IP")).thenReturn("127.0.0.1");
        ratingService.addStar(44L, 5, request);
    }
}
