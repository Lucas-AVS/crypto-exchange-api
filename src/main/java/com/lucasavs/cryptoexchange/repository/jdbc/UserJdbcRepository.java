package com.lucasavs.cryptoexchange.repository.jdbc;

import com.lucasavs.cryptoexchange.entity.User;
import com.lucasavs.cryptoexchange.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Profile("jdbc") // só ativa este bean quando o profile 'jdbc' está ativo
public class UserJdbcRepository implements UserRepository {

    private final JdbcTemplate template;

    public UserJdbcRepository(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public User save(User user) {
        String sql = """
            INSERT INTO users (email, password_hash)
            VALUES (?, ?)
            RETURNING id, email, password_hash, created_at
        """;

        return template.queryForObject(
                sql,
                userRowMapper(),
                user.getEmail(),
                user.getPasswordHash()
        );
    }

    @Override
    public Optional<User> findById(UUID id) {
        String sql = """
            SELECT id, email, password_hash, created_at
            FROM users
            WHERE id = ?
        """;

        List<User> list = template.query(sql, userRowMapper(), id);
        return list.stream().findFirst();
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT id, email, password_hash, created_at FROM users";
        return template.query(sql, userRowMapper());
    }

    @Override
    public void deleteById(UUID theId) {
        String sql = """
            DELETE
            FROM users
            WHERE id = ?
        """;
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> new User(
                rs.getObject("id", UUID.class),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getTimestamp("created_at")
        );
    }
}
