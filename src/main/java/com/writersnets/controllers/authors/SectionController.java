package com.writersnets.controllers.authors;

import com.writersnets.models.response.SectionResponse;
import com.writersnets.services.authors.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by mhenr on 20.10.2017.
 */
@RestController
@CrossOrigin
public class SectionController {
    private SectionService sectionService;

    @Autowired
    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @RequestMapping(value = "sections/{sectionId:.+}", method = RequestMethod.GET)
    public ResponseEntity<?> getSection(@PathVariable final Long sectionId) {
        SectionResponse section = sectionService.getSection(sectionId);
        return new ResponseEntity<>(section, HttpStatus.OK);
    }
}
