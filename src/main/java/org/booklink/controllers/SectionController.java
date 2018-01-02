package org.booklink.controllers;

import org.booklink.models.Response;
import org.booklink.models.entities.Section;
import org.booklink.models.entities.User;
import org.booklink.models.response.SectionResponse;
import org.booklink.repositories.AuthorRepository;
import org.booklink.repositories.SectionRepository;
import org.booklink.services.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.web.bind.annotation.*;

/**
 * Created by mhenr on 20.10.2017.
 */
@RestController
public class SectionController {
    private SectionService sectionService;

    @Autowired
    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @CrossOrigin
    @RequestMapping(value = "sections/{sectionId:.+}", method = RequestMethod.GET)
    public ResponseEntity<?> getSection(@PathVariable Long sectionId) {
        SectionResponse section = sectionService.getSection(sectionId);
        if (section != null) {
            return new ResponseEntity<>(section, HttpStatus.OK);
        }
        Response<String> response = new Response<>();
        response.setCode(1);
        response.setMessage("Section not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
