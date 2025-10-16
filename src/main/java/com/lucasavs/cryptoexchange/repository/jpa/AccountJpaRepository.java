package com.lucasavs.cryptoexchange.repository.jpa;

import com.lucasavs.cryptoexchange.entity.Account;
import com.lucasavs.cryptoexchange.repository.AccountRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Profile("jpa")
public interface AccountJpaRepository extends JpaRepository<Account, UUID>, AccountRepository {
}