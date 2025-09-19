package com.lucasavs.cryptoexchange.service;

import com.lucasavs.cryptoexchange.dto.UserCreateRequest;
import com.lucasavs.cryptoexchange.dto.UserDto;
import com.lucasavs.cryptoexchange.dto.UserUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserDto> findAll();

    UserDto findById(UUID theId);

    UserDto save(UserCreateRequest theUser);

    UserDto update(UUID id, UserUpdateRequest in);

    void deleteById(UUID theId);
}
