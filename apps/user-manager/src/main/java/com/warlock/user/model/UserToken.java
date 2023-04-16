package com.warlock.user.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserToken {
    @NotNull
    private String token;
}
