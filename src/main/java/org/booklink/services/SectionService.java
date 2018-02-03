package org.booklink.services;

import org.booklink.models.entities.Section;
import org.booklink.models.entities.User;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.response.SectionResponse;
import org.booklink.repositories.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mhenr on 20.11.2017.
 */
@Service
@Transactional(readOnly = true)
public class SectionService {
    private SectionRepository sectionRepository;

    @Autowired
    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public SectionResponse getSection(final Long sectionId) {
        SectionResponse section = sectionRepository.getSectionById(sectionId);
        if (section == null) {
            throw new ObjectNotFoundException("Section is not found");
        }
        return section;
    }
}
