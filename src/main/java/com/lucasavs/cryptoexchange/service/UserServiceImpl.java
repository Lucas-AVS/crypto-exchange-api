package com.lucasavs.cryptoexchange.service;

import com.lucasavs.cryptoexchange.dto.UserCreateRequest;
import com.lucasavs.cryptoexchange.dto.UserDto;
import com.lucasavs.cryptoexchange.dto.UserUpdateRequest;
import com.lucasavs.cryptoexchange.entity.User;
import com.lucasavs.cryptoexchange.exception.ResourceAlreadyExistException;
import com.lucasavs.cryptoexchange.exception.ResourceNotFoundException;
import com.lucasavs.cryptoexchange.mapper.UserMapper;
import com.lucasavs.cryptoexchange.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
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
        return userList.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found."));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto save(UserCreateRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new ResourceAlreadyExistException("User with email: '" + req.getEmail() + " already exists.");
        }
        User toPersist = new User();
        toPersist.setEmail(req.getEmail());

        // Todo: add hashing

        toPersist.setPasswordHash(req.getPassword());
        User persisted = userRepository.save(toPersist);
        return userMapper.toDto(persisted);
    }

    @Override
    public UserDto update(UUID id, UserUpdateRequest req) {
        User userFromDb = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found."));
        if (userRepository.existsByEmail(req.getEmail()) && !userFromDb.getEmail().equals(req.getEmail())) {
            throw new ResourceAlreadyExistException("User with email: '" + req.getEmail() + " already exists.");
        }
        else {
            if (req.getEmail() != null) {
                userFromDb.setEmail(req.getEmail());
            }
            if (req.getPassword() != null && !req.getPassword().isBlank()) {
                userFromDb.setPasswordHash(req.getPassword());
            }

            User saved = userRepository.save(userFromDb);
            return userMapper.toDto(saved);
        }
    }

    @Override
    public void deleteById(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with ID " + id + " not found.");
        }
        userRepository.deleteById(id);
    }
}