package com.lucasavs.cryptoexchange.repo;

import com.lucasavs.cryptoexchange.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserJdbcRepo implements UserRepoI {

    private final JdbcTemplate template;

    public UserJdbcRepo(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public int save(User user) {
        String sql = "INSERT INTO users (id, email, password_hash, created_at) VALUES (?, ?, ?, ?)";
        return template.update(sql,
                user.getId(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getCreatedAt());
    }

    @Override
    public Optional<User> findById(UUID id) {
        String sql = "SELECT id, email, password_hash, created_at FROM users WHERE id = ?";
        List<User> list = template.query(sql, userRowMapper(), id);
        return list.stream().findFirst();
    }

    @Override
    public List<User> findAll() {
        return template.query("SELECT * FROM users", userRowMapper());
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
