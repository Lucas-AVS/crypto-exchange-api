package com.lucasavs.cryptoexchange.repository;

import com.lucasavs.cryptoexchange.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    List<User> findAll();

    Optional<User> findById(UUID id);

    boolean existsByEmail(String email);

    boolean existsById(UUID id);

    <S extends User> S save(S user);

    void deleteById(UUID theId);
}
