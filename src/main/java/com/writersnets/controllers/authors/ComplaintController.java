package com.writersnets.controllers.authors;

import com.writersnets.models.Response;
import com.writersnets.models.entities.users.Complaint;
import com.writersnets.models.request.ComplaintRequest;
import com.writersnets.models.response.ComplaintResponse;
import com.writersnets.services.authors.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("complaints")
public class ComplaintController {
    private ComplaintService service;

    @Autowired
    public ComplaintController(ComplaintService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/users/{userId}")
    public Page<ComplaintResponse> getAllComplaints(@PathVariable String userId, Pageable pageable) {
        return service.getAllComplaints(userId, pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{complaintId}")
    public ResponseEntity<?> getComplaintDetails(@PathVariable Long complaintId) {
        ComplaintResponse result = service.getComplaintDetails(complaintId);
        return Response.createResponseEntity(0, result, null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<?> makeComplaint(@RequestBody ComplaintRequest request, Principal principal) {
        Complaint result = service.makeComplaint(request, principal);
        return Response.createResponseEntity(0, "Complaint with id " + result.getId() + " was made", null, HttpStatus.OK);
    }
}
