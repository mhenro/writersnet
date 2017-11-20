package org.booklink.services;

import org.booklink.models.Response;
import org.booklink.models.entities.Section;
import org.booklink.models.entities.User;
import org.booklink.repositories.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by mhenr on 20.11.2017.
 */
@Service
public class SectionService {
    private SectionRepository sectionRepository;

    @Autowired
    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public Section getSection(final Long sectionId) {
        Section section = sectionRepository.findOne(sectionId);
        if (section != null) {
            User author = section.getAuthor();
            hideAuthInfo(author);
        }
        return section;
    }

    private void hideAuthInfo(User user) {
        user.setPassword("");
        user.setActivationToken("");
        user.setAuthority("");
    }
}
