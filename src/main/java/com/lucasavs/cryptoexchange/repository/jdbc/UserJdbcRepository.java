package com.lucasavs.cryptoexchange.repository.jdbc;

import com.lucasavs.cryptoexchange.entity.User;
import com.lucasavs.cryptoexchange.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;

@Repository
@Profile("jdbc")
public class UserJdbcRepository implements UserRepository {

    private final JdbcTemplate template;

    private static final String INSERT_USER_SQL = """
        INSERT INTO users (email, password_hash)
        VALUES (?, ?)
        RETURNING id, email, password_hash, created_at
    """;
    private static final String UPDATE_USER_SQL = """
        UPDATE users
        SET email = COALESCE(?, email)
            password_hash = COALESCE(?, password_hash)
        WHERE id = ?
        RETURNING id, email, password_hash, created_at
    """;
    private static final String SELECT_USER_BY_ID_SQL = """
        SELECT id, email, password_hash, created_at
        FROM users
        WHERE id = ?
    """;
    private static final String SELECT_USER_BY_EMAIL_SQL = """
        SELECT id, email, password_hash, created_at
        FROM users
        WHERE email = ?
    """;
    private static final String SELECT_ALL_USERS_SQL = """
        SELECT id, email, password_hash, created_at
        FROM users
    """;
    private static final String DELETE_USER_BY_ID_SQL = """
        DELETE
        FROM users
        WHERE id = ?
    """;

    public UserJdbcRepository(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            // INSERT
            return template.queryForObject(
                    INSERT_USER_SQL,
                    userRowMapper(),
                    user.getEmail(),
                    user.getPasswordHash()
            );
        } else {
            // UPDATE (keep values when null)
            String sql = UPDATE_USER_SQL;
            return template.queryForObject(
                    sql,
                    userRowMapper(),
                    user.getEmail(),
                    user.getPasswordHash(),
                    user.getId()
            );
        }
    }

    @Override
    public Optional<User> findById(UUID id) {
        String sql = SELECT_USER_BY_ID_SQL;

        List<User> list = template.query(sql, userRowMapper(), id);
        return list.stream().findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = SELECT_USER_BY_EMAIL_SQL;

        List<User> list = template.query(sql, userRowMapper(), email);
        return list.stream().findFirst();
    }

    @Override
    public List<User> findAll() {
        return template.query(SELECT_ALL_USERS_SQL, userRowMapper());
    }

    @Override
    public void deleteById(UUID theId) {
        template.update(DELETE_USER_BY_ID_SQL, theId);
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getObject("id", UUID.class));
            user.setEmail(rs.getString("email"));
            user.setPasswordHash(rs.getString("password_hash"));
            user.setCreatedAt(rs.getObject("created_at", Instant.class));

            return user;
        };
    }
}
