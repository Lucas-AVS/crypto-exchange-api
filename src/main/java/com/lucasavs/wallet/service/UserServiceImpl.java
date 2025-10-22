package com.lucasavs.wallet.service;

import com.lucasavs.wallet.dto.UserCreateRequest;
import com.lucasavs.wallet.dto.UserDto;
import com.lucasavs.wallet.dto.UserLoginRequest;
import com.lucasavs.wallet.dto.UserUpdateRequest;
import com.lucasavs.wallet.entity.User;
import com.lucasavs.wallet.exception.ResourceAlreadyExistException;
import com.lucasavs.wallet.exception.ResourceNotFoundException;
import com.lucasavs.wallet.mapper.UserMapper;
import com.lucasavs.wallet.repository.UserRepository;
import com.lucasavs.wallet.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder,
                           @Lazy AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Override
    public String login(UserLoginRequest request) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        return tokenService.generateToken((User) auth.getPrincipal());
    }


    @Override
    public UserDto save(UserCreateRequest req) {
        User toPersist = new User();
        toPersist.setEmail(req.getEmail());
        toPersist.setPasswordHash(passwordEncoder.encode(req.getPassword()));

        try {
            User persisted = userRepository.save(toPersist);
            return userMapper.toDto(persisted);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceAlreadyExistException("User with email: '" + req.getEmail() + " already exists.");
        }
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
    public UserDto update(UUID id, UserUpdateRequest req) {
        User userFromDb = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found."));

        if (req.getEmail() != null) {
            userFromDb.setEmail(req.getEmail());
        }
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            userFromDb.setPasswordHash(passwordEncoder.encode(req.getPassword()));
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