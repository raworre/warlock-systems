package com.warlock.user.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document("profile")
@Data
public class Profile {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    @DBRef
    private User user;
}
