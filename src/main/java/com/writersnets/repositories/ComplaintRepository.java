package com.writersnets.repositories;

import com.writersnets.models.entities.users.Complaint;
import com.writersnets.models.response.ComplaintResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ComplaintRepository extends PagingAndSortingRepository<Complaint, Long> {
    @Query("SELECT new com.writersnets.models.response.ComplaintResponse(c.id, c.culprit.username, c.culprit.firstName, c.culprit.lastName," +
            "c.author.username, c.author.firstName, c.author.lastName, c.text) FROM Complaint c WHERE c.culprit.username = ?1")
    Page<ComplaintResponse> getAllComplaints(String userId, Pageable pageable);

    @Query("SELECT new com.writersnets.models.response.ComplaintResponse(c.id, c.culprit.username, c.culprit.firstName, c.culprit.lastName," +
            "c.author.username, c.author.firstName, c.author.lastName, c.text) FROM Complaint c WHERE c.id = ?1")
    ComplaintResponse getComplaintDetails(Long complaintId);
}