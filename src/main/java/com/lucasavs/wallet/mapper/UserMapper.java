package com.lucasavs.wallet.mapper;

import com.lucasavs.wallet.dto.UserDto;
import com.lucasavs.wallet.entity.User;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {
    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public User toEntity(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        return user;
    }
}