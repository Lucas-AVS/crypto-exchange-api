package com.lucasavs.wallet.repository.jpa;

import com.lucasavs.wallet.entity.User;
import com.lucasavs.wallet.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Profile("jpa")
public interface UserJpaRepository
        extends JpaRepository<User, UUID>, UserRepository {
    // custom query ex: Optional<User> findByEmail(String email);
}
