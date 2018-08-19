package com.writersnets.repositories;

import com.writersnets.models.entities.groups.ChatGroup;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by mhenr on 10.12.2017.
 */
public interface ChatGroupRepository extends PagingAndSortingRepository<ChatGroup, Long> {
}
