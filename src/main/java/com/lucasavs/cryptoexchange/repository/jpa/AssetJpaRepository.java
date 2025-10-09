package com.lucasavs.cryptoexchange.repository.jpa;

import com.lucasavs.cryptoexchange.entity.Asset;
import com.lucasavs.cryptoexchange.repository.AssetRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Profile({"jpa", "jpa-test"})
public interface AssetJpaRepository extends JpaRepository<Asset, String>, AssetRepository {
    // custom query ex: Optional<User> findByEmail(String email);
}
