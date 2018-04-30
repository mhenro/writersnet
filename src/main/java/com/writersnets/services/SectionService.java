package com.writersnets.services;

import com.writersnets.models.entities.Section;
import com.writersnets.models.entities.User;
import com.writersnets.models.exceptions.ObjectNotFoundException;
import com.writersnets.models.response.SectionResponse;
import com.writersnets.repositories.SectionRepository;
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
