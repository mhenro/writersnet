package org.booklink.repositories;

import org.booklink.models.entities.Section;
import org.booklink.models.response.SectionResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by mhenr on 20.10.2017.
 */
public interface SectionRepository extends CrudRepository<Section, Long> {
    @Query("SELECT new org.booklink.models.response.SectionResponse(s.id, s.name, s.description, s.lastUpdated) FROM Section s WHERE s.id = ?1")
    SectionResponse getSectionById(final Long id);
}
