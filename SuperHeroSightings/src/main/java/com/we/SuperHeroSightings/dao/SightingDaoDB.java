
package com.we.SuperHeroSightings.dao;

import com.we.SuperHeroSightings.entities.Hero;
import com.we.SuperHeroSightings.entities.Location;
import com.we.SuperHeroSightings.entities.Sighting;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.we.SuperHeroSightings.dao.LocationDaoDB.LocationMapper;
import com.we.SuperHeroSightings.dao.HeroDaoDB.HeroMapper;

/**
 *
 * @author jtriolo
 */
@Repository
public class SightingDaoDB implements SightingDao {
    
    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Sighting getSightingByID(int id) {
        final String GET_SIGHTING_BY_ID = "SELECT * FROM Sighting WHERE SightingPK = ?";
        try{
            Sighting sighting = jdbc.queryForObject(GET_SIGHTING_BY_ID, new SightingMapper(), id);
            sighting.setHero(getHeroForSighting(id));
            sighting.setLocation(getLocationForSighting(id));
            return sighting;
        }catch(DataAccessException ex){
            return null;
        }
    }

    private Hero getHeroForSighting(int id){
        final String SELECT_HERO_FOR_SIGHTING = "SELECT h.* FROM Hero h " +
                "INNER JOIN Sighting s " +
                "ON h.HeroPK = s.HeroPK " +
                "WHERE s.SightingPK = ?";
        try {
            return jdbc.queryForObject(SELECT_HERO_FOR_SIGHTING, new HeroMapper(), id);
        } catch(DataAccessException ex){
            return null;
        }
    }

    private Location getLocationForSighting(int id){
        final String SELECT_LOCATION_FOR_SIGHTING = "SELECT l.* FROM Location l " +
                "INNER JOIN Sighting s " +
                "ON l.LocationPK = s.LocationPK " +
                "WHERE s.SightingPK = ?";
        try{
            return jdbc.queryForObject(SELECT_LOCATION_FOR_SIGHTING, new LocationMapper(), id);
        } catch(DataAccessException ex){
            return null;
        }
    }

    private void associateHerosAndLocations(List<Sighting> sightings){
        for (Sighting sighting: sightings){
            sighting.setHero(getHeroForSighting(sighting.getId()));
            sighting.setLocation(getLocationForSighting(sighting.getId()));
        }
    }

    @Override
    public List<Sighting> getAllSightings() {
        final String SELECT_SIGHTING = "SELECT * FROM sighting";
        List<Sighting> sightings = jdbc.query(SELECT_SIGHTING, new SightingMapper());
        associateHerosAndLocations(sightings);
        return sightings;
    }

    @Override
    public Sighting addSighting(Sighting sighting) {
        final String INSERT_SIGHTING = "INSERT INTO sighting(sightingDate, description, heroPK, locationPK) "
                + "VALUES(?,?,?,?)";
        jdbc.update(INSERT_SIGHTING,
                sighting.getDate(),
                sighting.getDescription(),
                sighting.getHero().getId(),
                sighting.getLocation().getId());
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        sighting.setId(newId);
        return sighting;
    }

    @Override
    public void updateSighting(Sighting sighting) {
        final String UPDATE_COURSE = "UPDATE Sighting SET sightingDate = ?, description = ?, "
                + "heroPK = ?, locationPk = ? WHERE SightingPK = ?";
        jdbc.update(UPDATE_COURSE,
                sighting.getDate(),
                sighting.getDescription(),
                sighting.getHero().getId(),
                sighting.getLocation().getId(),
                sighting.getId());
    }

    @Override
    public void deleteSightingByID(int id) {
        final String DELETE_SIGHTING = "DELETE FROM Sighting WHERE SightingPK = ?";
        jdbc.update(DELETE_SIGHTING, id);
    }

    @Override
    public List<Sighting> getSightingsByDate(LocalDateTime date) {
        final String SELECT_ALL_SIGHTINGS = "SELECT * FROM sighting WHERE SightingDate = ?";
        List<Sighting> sightings = jdbc.query(SELECT_ALL_SIGHTINGS, new SightingMapper(), date);
        associateHerosAndLocations(sightings);
        return sightings;
    }

    @Override
    public List<Sighting> getSightingsByLocation(Location location) {
        final String SELECT_ALL_SIGHTINGS = "SELECT * FROM sighting WHERE LocationPK = ?";
        List<Sighting> sightings = jdbc.query(SELECT_ALL_SIGHTINGS, new SightingMapper(), location.getId());
        associateHerosAndLocations(sightings);
        return sightings;
    }

    @Override
    public List<Sighting> getSightingsByHero(Hero hero) {
        final String SELECT_ALL_SIGHTINGS = "SELECT * FROM sighting WHERE HeroPK = ?";
        List<Sighting> sightings = jdbc.query(SELECT_ALL_SIGHTINGS, new SightingMapper(), hero.getId());
        associateHerosAndLocations(sightings);
        return sightings;
    }

    public class SightingMapper implements RowMapper<Sighting> {
        @Override
        public Sighting mapRow(ResultSet rs, int index) throws SQLException {
            Sighting sighting = new Sighting();
            sighting.setId(rs.getInt("SightingPK"));
            sighting.setDescription(rs.getString("Description"));
            sighting.setDate(rs.getTimestamp("sightingDate").toLocalDateTime());
            return sighting;
        }
    }
    

    
}
