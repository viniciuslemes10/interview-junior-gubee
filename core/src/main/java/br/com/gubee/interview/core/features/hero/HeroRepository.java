package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.model.ComparandoHero;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.enums.Race;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;



@Repository
@RequiredArgsConstructor
public class HeroRepository {
    @Autowired
    private DataSource dataSource;

    private static final Logger logger = LogManager.getLogger(HeroRepository.class);

    private static final String CREATE_HERO_QUERY = "INSERT INTO hero" +
            " (name, race, power_stats_id)" +
            " VALUES (:name, :race, :powerStatsId) RETURNING id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    UUID create(Hero hero) {
        final Map<String, Object> params = Map.of("name", hero.getName(),
                "race", hero.getRace().name(),
                "powerStatsId", hero.getPowerStatsId());

        logger.debug("Create Hero params: {}", params);

        return namedParameterJdbcTemplate.queryForObject(
                CREATE_HERO_QUERY,
                params,
                UUID.class);
    }

    private static final String SELECT_HERO_QUERY = "SELECT hero.name, hero.race, " +
            "power_stats.strength, power_stats.agility, " +
            "power_stats.dexterity, power_stats.intelligence " +
            "FROM hero " +
            "INNER JOIN power_stats ON hero.power_stats_id = power_stats.id " +
            "WHERE hero.id = UUID(?::varchar)";

    public ResponseEntity<Map<String, Object>> buscarHerois(String id) {
        try {
            UUID uuid = UUID.fromString(id);

            final Map<String, Object> result = namedParameterJdbcTemplate.getJdbcOperations().queryForMap(
                    SELECT_HERO_QUERY,
                    uuid.toString()
            );

            return ResponseEntity.ok(result);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private static final String SELECT_HERO_NAME_QUERY = "SELECT hero.name, hero.race, " +
            "power_stats.strength, power_stats.agility, " +
            "power_stats.dexterity, power_stats.intelligence " +
            "FROM hero " +
            "INNER JOIN power_stats ON hero.power_stats_id = power_stats.id " +
            "WHERE hero.name = :name";

    public ResponseEntity<Map<String, Object>> buscarHeroPorNome(String name) {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("name", name);

            final Map<String, Object> result = namedParameterJdbcTemplate.queryForMap(
                    SELECT_HERO_NAME_QUERY,
                    parameters
            );

            return ResponseEntity.ok(result);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.ok(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(null);
        }
    }


    public ResponseEntity<Hero> atualizarHeroi(Hero heroAtualizado, String id) {

        String sql = "UPDATE hero SET name = ?, race = ?, updated_at = now() WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, heroAtualizado.getName());
            statement.setString(2, heroAtualizado.getRace().toString());
            statement.setObject(3, UUID.fromString(id));

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                return ResponseEntity.ok(heroAtualizado);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private static final String SELECT_HERO_COMPARE_NAME_QUERY = "SELECT hero.id, hero.name, hero.race, " +
            "power_stats.id, power_stats.strength, power_stats.agility, " +
            "power_stats.dexterity, power_stats.intelligence " +
            "FROM hero " +
            "INNER JOIN power_stats ON hero.power_stats_id = power_stats.id " +
            "WHERE hero.name = :name";

    public ComparandoHero buscar(String name, ComparandoHero hero) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("name", name);

            Map<String, Object> result = namedParameterJdbcTemplate.queryForObject(
                    SELECT_HERO_COMPARE_NAME_QUERY,
                    params,
                    (rs, rowNum) -> {
                        Map<String, Object> row = new HashMap<>();
                        row.put("heroId", rs.getString("id"));
                        row.put("heroName", rs.getString("name"));
                        row.put("heroRace", rs.getString("race"));
                        row.put("powerStatsId", rs.getString("id"));
                        row.put("strength", rs.getInt("strength"));
                        row.put("agility", rs.getInt("agility"));
                        row.put("dexterity", rs.getInt("dexterity"));
                        row.put("intelligence", rs.getInt("intelligence"));
                        return row;
                    }
            );

            hero.setId(UUID.fromString((String) result.get("heroId")));
            hero.setName((String) result.get("heroName"));
            hero.setRace(Race.valueOf((String) result.get("heroRace")));

            hero.setId(UUID.fromString((String) result.get("powerStatsId")));
            hero.setStrength((int) result.get("strength"));
            hero.setAgility((int) result.get("agility"));
            hero.setDexterity((int) result.get("dexterity"));
            hero.setIntelligence((int) result.get("intelligence"));

            return hero;
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
