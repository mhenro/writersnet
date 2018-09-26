package com.writersnets.services.authors;

import com.writersnets.models.entities.users.Complaint;
import com.writersnets.models.entities.users.User;
import com.writersnets.models.exceptions.ObjectNotFoundException;
import com.writersnets.models.request.ComplaintRequest;
import com.writersnets.models.response.ComplaintResponse;
import com.writersnets.repositories.AuthorRepository;
import com.writersnets.repositories.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@Transactional(readOnly = true)
public class ComplaintService {
    private AuthorRepository authorRepo;
    private ComplaintRepository repo;

    @Autowired
    public ComplaintService(AuthorRepository authorRepo, ComplaintRepository repo) {
        this.authorRepo = authorRepo;
        this.repo = repo;
    }

    public Page<ComplaintResponse> getAllComplaints(String userId, Pageable pageable) {
        return repo.getAllComplaints(userId, pageable);
    }

    public ComplaintResponse getComplaintDetails(Long complaintId) {
        return repo.getComplaintDetails(complaintId);
    }

    @Transactional
    public Complaint makeComplaint(ComplaintRequest request, Principal principal) {
        User author = authorRepo.findById(principal.getName()).orElseThrow(() -> new ObjectNotFoundException("Author is not found"));
        User culprit = authorRepo.findById(request.getUserId()).orElseThrow(() -> new ObjectNotFoundException("Culprit is not found"));
        Complaint complaint = new Complaint();
        complaint.setAuthor(author);
        complaint.setCulprit(culprit);
        complaint.setText(request.getText());
        culprit.increaseComplaints();
        return repo.save(complaint);
    }
}
