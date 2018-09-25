package com.writersnets.models.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ComplaintRequest {
    private String userId;
    private String text;
}
