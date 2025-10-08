package com.lucasavs.cryptoexchange.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "accounts", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "asset_symbol"}))
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "balance", nullable = false, precision = 38, scale = 18)
    private BigDecimal balance;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_symbol", nullable = false)
    private Asset asset;

    public Account() {
    }
    public Account(User user, Asset assetSymbol) {
        this.user = user;
        this.asset = assetSymbol;
        this.balance = BigDecimal.ZERO;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Asset getAsset() { return asset; }
    public void setAsset(Asset assetSymbol) { this.asset = assetSymbol; }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                ", version=" + version +
                ", updatedAt=" + updatedAt +
                ", user=" + user +
                ", asset=" + asset +
                '}';
    }
}