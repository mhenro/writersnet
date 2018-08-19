package com.writersnets.repositories;

import com.writersnets.models.entities.books.BookText;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mhenr on 01.11.2017.
 */
public interface BookTextRepository extends CrudRepository<BookText, Long> {
}
