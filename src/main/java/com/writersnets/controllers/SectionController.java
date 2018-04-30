package com.writersnets.controllers;

import com.writersnets.models.Response;
import com.writersnets.models.entities.Section;
import com.writersnets.models.entities.User;
import com.writersnets.models.exceptions.ObjectNotFoundException;
import com.writersnets.models.exceptions.UnauthorizedUserException;
import com.writersnets.models.response.SectionResponse;
import com.writersnets.repositories.AuthorRepository;
import com.writersnets.repositories.SectionRepository;
import com.writersnets.services.SectionService;
import com.writersnets.utils.ControllerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.StreamingHttpOutputMessage;
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

    /* ---------------------------------------exception handlers-------------------------------------- */

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> objectNotFound(ObjectNotFoundException e) {
        return Response.createResponseEntity(5, ControllerHelper.getErrorOrDefaultMessage(e, "Object is not found"), null, HttpStatus.NOT_FOUND);
    }
}
