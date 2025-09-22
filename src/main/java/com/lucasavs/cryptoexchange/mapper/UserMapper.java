package com.lucasavs.cryptoexchange.mapper;

import com.lucasavs.cryptoexchange.dto.UserCreateRequest;
import com.lucasavs.cryptoexchange.dto.UserDto;
import com.lucasavs.cryptoexchange.dto.UserUpdateRequest;
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

    public UserCreateRequest toCreatedDto(User user) {
        UserCreateRequest dto = new UserCreateRequest();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPasswordHash());
        return dto;
    }

    public UserUpdateRequest toUpdateDto(User user) {
        UserUpdateRequest dto = new UserUpdateRequest();
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