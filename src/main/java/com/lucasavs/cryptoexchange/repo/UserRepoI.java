package com.lucasavs.cryptoexchange.repo;

import com.lucasavs.cryptoexchange.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepoI {
    int save(User user);
    Optional<User> findById(UUID id);
    List<User> findAll();
}
