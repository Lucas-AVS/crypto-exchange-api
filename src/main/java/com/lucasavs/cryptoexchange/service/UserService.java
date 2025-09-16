package com.lucasavs.cryptoexchange.service;

import com.lucasavs.cryptoexchange.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> findAll();

    User findById(UUID theId);

    User save(User theUser);

    void deleteById(UUID theId);
}

