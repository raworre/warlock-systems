package com.warlock.user.service;

import org.springframework.security.core.AuthenticationException;
public class UsernameAlreadyExistsException extends AuthenticationException {
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
