package com.lucasavs.cryptoexchange.repo;

import com.lucasavs.cryptoexchange.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsersRepo extends JpaRepository<User, UUID> {
}
