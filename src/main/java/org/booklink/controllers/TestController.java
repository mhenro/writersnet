
package org.booklink.controllers;

import org.booklink.models.entities.Book;
import org.booklink.repositories.BookRepository;
import org.booklink.services.TestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mhenr on 27.09.2017.
 */
public class TestController {
    private TestBean testBean;
    private BookRepository testRepo;

    @Autowired
    public TestController(TestBean testBean, BookRepository testRepo) {
        this.testBean = testBean;
        this.testRepo = testRepo;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("test")
    public List<String> test() {
        List<String> result = new ArrayList<>();
        result.add(testBean.getMessage());
        result.add("3");
        result.add("2");
        result.add("1");
        return result;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("test2")
    public List<String> test2() {
        List<String> result = new ArrayList<>();
        result.add(testBean.getMessage());
        return result;
    }

    @RequestMapping("book")
    public Page<Book> book() {
        Page<Book> books = testRepo.findAll(new PageRequest(0, 20));
        return books;
    }
}
