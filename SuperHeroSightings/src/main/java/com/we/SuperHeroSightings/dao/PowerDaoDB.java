
package com.we.SuperHeroSightings.dao;

import com.we.SuperHeroSightings.entities.Power;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jtriolo
 */
@Repository
public class PowerDaoDB implements PowerDao {

    @Autowired
    JdbcTemplate jdbc;

    final String GET_POWER_BY_ID = "SELECT * FROM power WHERE powerPK = ?";
    final String GET_ALL_POWERS = "SELECT * FROM power";
    final String INSERT_POWER= "INSERT INTO power (power, description) VALUES (?, ?)";
    final String GET_INSERT_ID = "SELECT LAST_INSERT_ID()";
    final String UPDATE_POWER = "UPDATE power SET power = ?, description = ? WHERE powerPK = ?";
    final String UPDATE_HERO_POWERPK = "UPDATE hero SET powerPK = null WHERE powerPK = ?";
    final String DELETE_POWER = "DELETE FROM power WHERE powerPK = ?";


    @Override
    public Power getPowerByID(int id) {
        try{
            return jdbc.queryForObject(GET_POWER_BY_ID, new PowerMapper(), id);
        }catch(DataAccessException ex){
            return null;
        }
    }

    @Override
    public List<Power> getAllPowers() {
        return jdbc.query(GET_ALL_POWERS, new PowerMapper());
    }

    @Override
    @Transactional
    public Power addPower(Power power) {
        jdbc.update(INSERT_POWER,
                power.getName(),
                power.getDescription());
        int newID = jdbc.queryForObject(GET_INSERT_ID, Integer.class);
        power.setId(newID);
        return power;
    }

    @Override
    public void updatePower(Power power) {
        jdbc.update(UPDATE_POWER,
                power.getName(),
                power.getDescription(),
                power.getId());
    }

    @Override
    @Transactional
    public void deletePowerByID(int id) {
        jdbc.update(UPDATE_HERO_POWERPK, id);
        jdbc.update(DELETE_POWER, id);
    }

    public static final class PowerMapper implements RowMapper<Power>{

        @Override
        public Power mapRow(ResultSet rs, int rowNum) throws SQLException {
            Power power = new Power();
            power.setId(rs.getInt("powerPK"));
            power.setName(rs.getString("power"));
            power.setDescription(rs.getString("description"));

            return power;
        }
    }

}
