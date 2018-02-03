package org.booklink.repositories;

import org.booklink.models.entities.UserBook;
import org.booklink.models.entities.UserBookPK;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by mhenr on 03.02.2018.
 */
public interface UserBookRepository extends PagingAndSortingRepository<UserBook, UserBookPK> {
}
