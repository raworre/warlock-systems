package com.warlock.user.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ProfileUpdateRequest {
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
}
