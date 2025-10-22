package com.lucasavs.wallet.service;

import com.lucasavs.wallet.dto.UserCreateRequest;
import com.lucasavs.wallet.dto.UserDto;
import com.lucasavs.wallet.dto.UserLoginRequest;
import com.lucasavs.wallet.dto.UserUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {
    String login(UserLoginRequest request);

    List<UserDto> findAll();

    UserDto findById(UUID theId);

    UserDto save(UserCreateRequest theUser);

    UserDto update(UUID id, UserUpdateRequest in);

    void deleteById(UUID theId);
}