package com.lucasavs.cryptoexchange.mapper;

import com.lucasavs.cryptoexchange.dto.UserCreateDto;
import com.lucasavs.cryptoexchange.dto.UserDto;
import com.lucasavs.cryptoexchange.dto.UserUpdateDto;
import com.lucasavs.cryptoexchange.entity.User;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {
    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public UserCreateDto toCreatedDto(User user) {
        UserCreateDto dto = new UserCreateDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPasswordHash());
        return dto;
    }

    public UserUpdateDto toUpdateDto(User user) {
        UserUpdateDto dto = new UserUpdateDto();
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPasswordHash());
        return dto;
    }

    public User toEntity(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        return user;
    }
}