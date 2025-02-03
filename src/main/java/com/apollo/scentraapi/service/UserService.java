package com.apollo.scentraapi.service;

import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import com.apollo.scentraapi.apiPayload.exception.handler.UserHandler;
import com.apollo.scentraapi.auth.JwtUtil;
import com.apollo.scentraapi.converter.UserConverter;
import com.apollo.scentraapi.domain.User;
import com.apollo.scentraapi.dto.request.UserRequest;
import com.apollo.scentraapi.dto.response.UserResponse;
import com.apollo.scentraapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public UserResponse.UserSignUpResultDTO createUser(UserRequest.UserSignUpDTO request) {

        Optional<User> findUser = userRepository.findByEmail(request.getEmail()); // 이메일로 유저가 존재하는지 검사

        if (findUser.isPresent())
            throw new UserHandler(ErrorStatus.USER_ALREADY_EXIST);

        User newUser = UserConverter.toUser(request);
        User savedUser = userRepository.save(newUser);

        String accessToken = jwtUtil.createAccessToken(savedUser.getEmail());

        return UserResponse.UserSignUpResultDTO.builder()
                .userId(savedUser.getId())
                .accessToken(accessToken)
                .build();
    }
}
