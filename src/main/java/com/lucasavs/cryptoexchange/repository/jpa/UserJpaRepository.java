package com.lucasavs.cryptoexchange.repository.jpa;

import com.lucasavs.cryptoexchange.entity.User;
import com.lucasavs.cryptoexchange.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Profile({"jpa", "jpa-test"})
public interface UserJpaRepository
        extends JpaRepository<User, UUID>, UserRepository {
    // custom query ex: Optional<User> findByEmail(String email);
}
