package org.booklink.repositories;

import org.booklink.models.entities.Section;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mhenr on 20.10.2017.
 */
public interface SectionRepository extends CrudRepository<Section, Long> {
}
