package com.warlock.user.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserProfile {
    private String username;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private LocalDate registrationDate;
}
