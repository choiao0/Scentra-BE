package com.apollo.scentraapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import com.apollo.scentraapi.apiPayload.exception.handler.UserException;
import com.apollo.scentraapi.auth.JwtUtil;
import com.apollo.scentraapi.dto.request.UserRequest;
import com.apollo.scentraapi.dto.response.UserResponse;
import com.apollo.scentraapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @DisplayName("새로운 유저가 회원가입한다.")
    @Test
    void createUser_success() throws Exception {
        // given
        UserRequest.UserSignUpDTO request = new UserRequest.UserSignUpDTO();
        ReflectionTestUtils.setField(request, "name", "testUser");
        ReflectionTestUtils.setField(request, "email", "testUser@example.com");

        UserResponse.UserSignUpResultDTO response = mock(UserResponse.UserSignUpResultDTO.class);

        given(userService.createUser(any()))
                .willReturn(response);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("성공입니다."))
                .andExpect(jsonPath("$.result").exists())
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("이메일 없이 회원가입하면 에러가 발생한다.")
    @Test
    void createUser_missingEmail() throws Exception {
        // given
        UserRequest.UserSignUpDTO request = new UserRequest.UserSignUpDTO();
        ReflectionTestUtils.setField(request, "name", "testUser");

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("COMMON400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("이메일 없이 회원가입하면 에러가 발생한다.")
    @Test
    void createUser_serviceThrowsException() throws Exception {
        // given
        UserRequest.UserSignUpDTO request = new UserRequest.UserSignUpDTO();
        ReflectionTestUtils.setField(request, "name", "testUser");
        ReflectionTestUtils.setField(request, "email", "testUser@example.com");

        given(userService.createUser(any())).willThrow(new UserException(ErrorStatus.USER_ALREADY_EXIST));

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("USER4002"))
                .andExpect(jsonPath("$.message").value("이미 존재하는 유저입니다."))
                .andDo(MockMvcResultHandlers.print());
    }
}
