package com.warlock.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warlock.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    static final LocalDate BIRTHDATE = LocalDate.parse("1981-06-18");

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService service;
}
