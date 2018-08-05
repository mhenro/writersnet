package com.writersnets.models.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Created by mhenr on 23.12.2017.
 */
@Getter @Setter @NoArgsConstructor
public class AuthorRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String sectionName;
    private String sectionDescription;
    private LocalDate birthday;
    private String city;
    private String language;
    private String preferredLanguages;
}