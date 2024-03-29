package com.warlock.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warlock.user.service.UserService;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    JwtParser jwtParser;

    @Autowired
    JwtBuilder jwtBuilder;

    @MockBean
    UserService service;
}
