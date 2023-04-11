package com.warlock.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class LoginRequest {
    @Schema(description = "Username of the user requesting login")
    @JsonProperty
    @NotNull
    private String username;

    @Schema(description = "Password of the user requesting login")
    @JsonProperty
    @NotNull
    private String password;
}
