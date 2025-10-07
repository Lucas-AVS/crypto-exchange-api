package com.lucasavs.cryptoexchange.repository.jdbc;

import com.lucasavs.cryptoexchange.entity.Asset;
import com.lucasavs.cryptoexchange.repository.AssetRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("jdbc")
public class AssetJdbcRepository implements AssetRepository {

    private final JdbcTemplate template;
    public AssetJdbcRepository(JdbcTemplate template) {
        this.template = template;
    }

    private static final String SELECT_ASSET_BY_SYMBOL_SQL = """
        SELECT symbol, kind, scale, name
        FROM assets
        WHERE symbol = ?
    """;
    private static final String SELECT_ALL_ASSETS_SQL = """
        SELECT symbol, kind, scale, name
        FROM assets
    """;

    @Override
    public Optional<Asset> findBySymbol(String symbol) {
        List<Asset> list = template.query(SELECT_ASSET_BY_SYMBOL_SQL, assetRowMapper(), symbol);
        return list.stream().findFirst();
    }

    @Override
    public List<Asset> findAll() {
        return template.query(SELECT_ALL_ASSETS_SQL, assetRowMapper());
    }

    private RowMapper<Asset> assetRowMapper() {
        return (rs, rowNum) -> new Asset(
                rs.getString("symbol"),
                rs.getString("kind"),
                rs.getInt("scale"),
                rs.getString("name")
        );
    }
}
