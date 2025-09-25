package com.lucasavs.cryptoexchange.service;

import com.lucasavs.cryptoexchange.dto.UserCreateRequest;
import com.lucasavs.cryptoexchange.dto.UserDto;
import com.lucasavs.cryptoexchange.dto.UserUpdateRequest;
import com.lucasavs.cryptoexchange.entity.User;
import com.lucasavs.cryptoexchange.exception.ResourceAlreadyExistException;
import com.lucasavs.cryptoexchange.exception.ResourceNotFoundException;
import com.lucasavs.cryptoexchange.mapper.UserMapper;
import com.lucasavs.cryptoexchange.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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
        User toPersist = new User();
        toPersist.setEmail(req.getEmail());
        // Todo: add hashing
        toPersist.setPasswordHash(req.getPassword());
        try {
            User persisted = userRepository.save(toPersist);
            return userMapper.toDto(persisted);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceAlreadyExistException("User with email: '" + req.getEmail() + " already exists.");
        }
    }

    @Override
    public UserDto update(UUID id, UserUpdateRequest req) {
        User userFromDb = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found."));

        if (req.getEmail() != null) {
            userFromDb.setEmail(req.getEmail());
        }
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            userFromDb.setPasswordHash(req.getPassword());
        }

        try {
            User saved = userRepository.save(userFromDb);
            return userMapper.toDto(saved);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceAlreadyExistException("User with email: '" + req.getEmail() + " already exists.");
        }
    }

    @Override
    public void deleteById(UUID id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("User with ID " + id + " not found.");
        }
    }
}