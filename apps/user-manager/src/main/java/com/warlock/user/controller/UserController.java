package com.warlock.user.controller;

import com.warlock.user.model.LoginRequest;
import com.warlock.user.model.RegistrationRequest;
import com.warlock.user.model.UserProfile;
import com.warlock.user.model.UserToken;
import com.warlock.user.service.UserService;
import com.warlock.user.service.UsernameAlreadyExistsException;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Logs in a user",
            description = "Takes user object and returns an access token that contains the given username from the request.")
    @ApiResponse(
            responseCode = "200",
            description = "Successfully logged in",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserToken.class)))
    @ApiResponse(
            responseCode = "400",
            description = "Request body is invalid",
            content = @Content(schema = @Schema(hidden = true)))
    @ApiResponse(
            responseCode = "401",
            description = "User not recognized",
            content = @Content(schema = @Schema(hidden = true)))
    public ResponseEntity<UserToken> login(
            @RequestBody @Valid LoginRequest loginRequest
    ) {
        return buildTokenResponse(userService.login(loginRequest));
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Registers new user",
            description = "Takes a registration object and creates a new user before returning a valid access token for future operations")
    @ApiResponse(
            responseCode = "200",
            description = "Successfully registered and logged in",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserToken.class)))
    @ApiResponse(
            responseCode = "400",
            description = "Registration is invalid",
            content = @Content(schema = @Schema(hidden = true)))
    public ResponseEntity<UserToken> register(
            @RequestBody @Valid RegistrationRequest registrationRequest
    ) {
        return buildTokenResponse(userService.register(registrationRequest));
    }

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Fetches user profile",
            description = "Returns the user profile based on the given token")
    @ApiResponse(
            responseCode = "200",
            description = "Successfully fetched user profile",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserProfile.UserProfileBuilder.class)))
    @ApiResponse(
            responseCode = "401",
            description = "User is not authorized",
            content = @Content(schema = @Schema(hidden = true)))
    public ResponseEntity<UserProfile> profile(
            @RequestHeader("Authorization") String authHeader
    ) throws AccessException {
        var token = authHeader.replace("Bearer ", "");
        return ok(userService.fetchProfile(token));
    }

    @ExceptionHandler(AccessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleInvalidToken() { }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleUserNotFound() { }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleBadCredentials() { }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleUsernameAlreadyExists() { }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleExpiredToken() { }

    private ResponseEntity<UserToken> buildTokenResponse(String token) {
        return status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(UserToken.builder().token(token).build());
    }
}
