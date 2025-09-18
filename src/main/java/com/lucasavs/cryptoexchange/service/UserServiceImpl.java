package com.lucasavs.cryptoexchange.service;

import com.lucasavs.cryptoexchange.dto.UserCreateDto;
import com.lucasavs.cryptoexchange.dto.UserDto;
import com.lucasavs.cryptoexchange.dto.UserUpdateDto;
import com.lucasavs.cryptoexchange.entity.User;
import com.lucasavs.cryptoexchange.mapper.UserMapper;
import com.lucasavs.cryptoexchange.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDto> findAll() {
        List<User> userList = userRepository.findAll();
        List<UserDto> dtoList = userList.stream()
                .map(user -> userMapper.toDto(user))// Map each entity to a DTO
                .collect(Collectors.toList()); // Collect the results into a List
        return dtoList;
    }

    @Override
    public UserDto findById(UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);

        User user;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }
        else {
            // user not found
            throw new RuntimeException("Did not find User id - " + id);
        }

        return userMapper.toDto(user);
    }

    @Override
    public UserCreateDto save(User user) {
        userRepository.save(user);
        return userMapper.toCreatedDto(user);
    }

    @Override
    public UserUpdateDto update(UUID id, UserUpdateDto user) {
        User userFromDb = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getEmail() != null) {
            userFromDb.setEmail(user.getEmail());
        }
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            userFromDb.setPasswordHash(user.getPassword());
        }

        User saved = userRepository.save(userFromDb);
        return userMapper.toUpdateDto(saved);
    }

    @Override
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }
}