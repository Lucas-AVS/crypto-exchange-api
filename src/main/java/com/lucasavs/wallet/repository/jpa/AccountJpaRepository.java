package com.lucasavs.wallet.repository.jpa;

import com.lucasavs.wallet.entity.Account;
import com.lucasavs.wallet.repository.AccountRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Profile("jpa")
public interface AccountJpaRepository extends JpaRepository<Account, UUID>, AccountRepository {
}