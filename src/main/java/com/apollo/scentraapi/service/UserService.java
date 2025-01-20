package com.apollo.scentraapi.service;

import com.apollo.scentraapi.apiPayload.code.status.ErrorStatus;
import com.apollo.scentraapi.apiPayload.exception.handler.UserHandler;
import com.apollo.scentraapi.converter.UserConverter;
import com.apollo.scentraapi.domain.User;
import com.apollo.scentraapi.dto.request.UserRequest;
import com.apollo.scentraapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User createUser(UserRequest.UserSignUpDTO request) {

        userRepository.findByEmail(request.getEmail()) //이메일로 유저가 존재하는지 검사
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_ALREADY_EXIST));

        User newUser = UserConverter.toUser(request);

        return userRepository.save(newUser);
    }
}
