package com.warlock.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class RegistrationRequest {
    @Schema(description = "Username of the new user")
    @JsonProperty
    @NotNull
    private String username;

    @Schema(description = "New password for new user")
    @JsonProperty
    @NotNull
    private String password;

    @Schema(description = "First name of new user")
    @JsonProperty
    @NotNull
    private String firstName;

    @Schema(description = "Last name of new user")
    @JsonProperty
    @NotNull
    private String lastName;

    @Schema(description = "Birthdate of new user")
    @JsonProperty
    @NotNull
    private LocalDate birthdate;
}
