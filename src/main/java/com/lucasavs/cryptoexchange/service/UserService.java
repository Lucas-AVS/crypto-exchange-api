package com.lucasavs.cryptoexchange.service;

import com.lucasavs.cryptoexchange.dto.UserCreateDto;
import com.lucasavs.cryptoexchange.dto.UserDto;
import com.lucasavs.cryptoexchange.dto.UserUpdateDto;
import com.lucasavs.cryptoexchange.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserDto> findAll();

    UserDto findById(UUID theId);

    UserCreateDto save(User theUser);

    UserUpdateDto update(UUID id, UserUpdateDto in);

    void deleteById(UUID theId);
}

