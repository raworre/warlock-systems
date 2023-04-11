package com.warlock.user.controller;

import com.warlock.user.model.LoginRequest;
import com.warlock.user.model.UserToken;
import com.warlock.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@Controller
@Slf4j
public class UserController {
    final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Logs in a user",
            description = "Takes user object and returns an access token that contains the given username from the request.")
    @ApiResponse(
            responseCode = "200",
            description = "Successfully logged in",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(
            responseCode = "400",
            description = "Request body is invalid",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)))
    @ApiResponse(
            responseCode = "401",
            description = "User not recognized",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)))
    public ResponseEntity<UserToken> login(
            @RequestBody @Valid LoginRequest loginRequest
    ) {
        log.info("Controller login called");
        var token = userService.login(loginRequest);
        return ok(UserToken.builder().token(token).build());
    }
}
