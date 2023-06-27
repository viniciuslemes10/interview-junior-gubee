package br.com.gubee.interview.core.features.powerstats;

import br.com.gubee.interview.model.PowerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PowerStatsRepository {
    @Autowired
    private DataSource dataSource;

    private static final String CREATE_POWER_STATS_QUERY = "INSERT INTO power_stats" +
        " (strength, agility, dexterity, intelligence)" +
        " VALUES (:strength, :agility, :dexterity, :intelligence) RETURNING id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    UUID create(PowerStats powerStats) {
        return namedParameterJdbcTemplate.queryForObject(
            CREATE_POWER_STATS_QUERY,
            new BeanPropertySqlParameterSource(powerStats),
            UUID.class);
    }

    private static final String SELECT_POWER_STATS_QUERY = "SELECT power_stats_id " +
            "FROM hero " +
            "WHERE id = UUID(?::varchar)";

    public String buscarPorId(String heroId) {
        try {
            UUID uuid = UUID.fromString(heroId);

            final Map<String, Object> result = namedParameterJdbcTemplate.getJdbcOperations().queryForMap(
                    SELECT_POWER_STATS_QUERY,
                    uuid.toString()
            );

            return result.get("power_stats_id").toString();
        } catch (EmptyResultDataAccessException e) {
            return null; // ou lançar uma exceção adequada
        } catch (IllegalArgumentException e) {
            return null; // ou lançar uma exceção adequada
        }
    }


    public ResponseEntity<Object> atualizaPower(PowerStats powerAtualizado, String id) {

        UUID uuid;

        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

        String sql = "UPDATE power_stats SET strength = ?, agility = ?, dexterity = ?, intelligence = ?, " +
                "updated_at = now() WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, powerAtualizado.getStrength());
            statement.setInt(2, powerAtualizado.getAgility());
            statement.setInt(3, powerAtualizado.getDexterity());
            statement.setInt(4, powerAtualizado.getIntelligence());
            statement.setObject(5, UUID.fromString(id));

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                return ResponseEntity.ok(powerAtualizado);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Object> excluirPower(String hero) {
        String powerStatsId = buscarPorId(hero);

        if (powerStatsId == null) {
            return ResponseEntity.notFound().build();
        }

        String deleteHeroSql = "DELETE FROM hero WHERE id = ?";
        String deletePowerStatsSql = "DELETE FROM power_stats WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement deleteHeroStatement = connection.prepareStatement(deleteHeroSql);
             PreparedStatement deletePowerStatsStatement = connection.prepareStatement(deletePowerStatsSql)) {

            // Excluir o registro da tabela 'hero'
            deleteHeroStatement.setObject(1, UUID.fromString(hero));
            int rowsDeletedFromHero = deleteHeroStatement.executeUpdate();

            // Excluir o registro da tabela 'power_stats'
            deletePowerStatsStatement.setObject(1, UUID.fromString(powerStatsId));
            int rowsDeletedFromPowerStats = deletePowerStatsStatement.executeUpdate();

            if (rowsDeletedFromHero > 0 && rowsDeletedFromPowerStats > 0) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }



}
