package com.writersnets.models.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ComplaintResponse {
    private Long id;
    private String userId;
    private String userFullName;
    private String authorId;
    private String authorFullName;
    private String complaintText;

    public ComplaintResponse(Long id, String userId, String userFirstName, String userLastName, String authorId,
                             String authorFirstName, String authorLastName, String text) {
        this.id = id;
        this.userId = userId;
        this.userFullName = userFirstName + " " + userLastName;
        this.authorId = authorId;
        this.authorFullName = authorFirstName + " " + authorLastName;
        this.complaintText = text;
    }
}
