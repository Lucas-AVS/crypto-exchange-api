package com.lucasavs.cryptoexchange.repository.jdbc;

import com.lucasavs.cryptoexchange.entity.Account;
import com.lucasavs.cryptoexchange.entity.Asset;
import com.lucasavs.cryptoexchange.entity.User;
import com.lucasavs.cryptoexchange.repository.AccountRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("jdbc")
public class AccountJdbcRepository implements AccountRepository {
    private static final String INSERT_ACCOUNT_SQL = """
            INSERT INTO accounts (user_id, asset_symbol, balance, version)
            VALUES (?, ?, 0, 0)
            RETURNING id, user_id, asset_symbol, balance, version, updated_at
            """;
    private static final String UPDATE_ACCOUNT_SQL = """
            UPDATE accounts
            SET balance = ?,
            version = version + 1
            WHERE id = ? AND version = ?
            RETURNING id, user_id, asset_symbol, balance, version, updated_at
            """;
    private static final String SELECT_ACCOUNT_BY_USER_ID_AND_ASSETS_SYMBOL_SQL = """
            SELECT id, user_id, asset_symbol, balance, version, updated_at
            FROM accounts
            WHERE user_id = ? AND asset_symbol = ?
            """;
    private static final String SELECT_ALL_ACCOUNTS_BY_USER_ID_SQL = """
            SELECT id, user_id, asset_symbol, balance, version, updated_at
            FROM accounts
            WHERE user_id = ?
            """;
    private final JdbcTemplate template;

    public AccountJdbcRepository(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Account save(Account account) {
        if (account.getId() == null) {
            // INSERT
            return template.queryForObject(
                    INSERT_ACCOUNT_SQL,
                    accountRowMapper(),
                    account.getUser().getId(),
                    account.getAsset().getSymbol()
            );
        } else {
            // UPDATE (keep values when null)
            return template.queryForObject(
                    UPDATE_ACCOUNT_SQL,
                    accountRowMapper(),
                    account.getBalance(),
                    account.getId(),
                    account.getVersion()
            );
        }
    }

    @Override
    public Optional<Account> findByUserIdAndAssetSymbol(UUID userId, String assetSymbol) {
        String sql = SELECT_ACCOUNT_BY_USER_ID_AND_ASSETS_SYMBOL_SQL;
        List<Account> list = template.query(sql, accountRowMapper(), userId, assetSymbol);
        return list.stream().findFirst();
    }

    @Override
    public List<Account> findByUserId(UUID userId) {
        return template.query(SELECT_ALL_ACCOUNTS_BY_USER_ID_SQL, accountRowMapper());
    }


    private RowMapper<Account> accountRowMapper() {
        return (rs, rowNum) -> {
            Account account = new Account();
            account.setId(rs.getObject("id", UUID.class));

            User user = new User();
            user.setId(rs.getObject("user_id", UUID.class));
            account.setUser(user);
            Asset asset = new Asset();
            asset.setSymbol(rs.getString("asset_symbol"));
            account.setAsset(asset);

            account.setBalance(rs.getBigDecimal("balance"));
            account.setVersion(rs.getLong("version"));
            account.setUpdatedAt(rs.getObject("updated_at", Instant.class));

            return account;
        };
    }
}
