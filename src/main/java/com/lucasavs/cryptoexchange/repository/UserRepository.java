package com.lucasavs.cryptoexchange.repository;

import com.lucasavs.cryptoexchange.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    <S extends User> S save(S user);

    Optional<User> findById(UUID id);

    List<User> findAll();
}
