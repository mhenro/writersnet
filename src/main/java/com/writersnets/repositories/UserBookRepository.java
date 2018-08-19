package com.writersnets.repositories;

import com.writersnets.models.entities.users.UserBook;
import com.writersnets.models.entities.users.UserBookPK;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by mhenr on 03.02.2018.
 */
public interface UserBookRepository extends PagingAndSortingRepository<UserBook, UserBookPK> {
}
