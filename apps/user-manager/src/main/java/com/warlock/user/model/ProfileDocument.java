package com.warlock.user.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document("profile")
@Data
@Builder
public class ProfileDocument {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private LocalDate registrationDate;
}
