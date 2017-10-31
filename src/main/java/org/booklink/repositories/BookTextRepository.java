package org.booklink.repositories;

import org.booklink.models.entities.BookText;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mhenr on 01.11.2017.
 */
public interface BookTextRepository extends CrudRepository<BookText, Long> {
}
