
package com.we.SuperHeroSightings.dao;


import com.we.SuperHeroSightings.entities.Hero;
import com.we.SuperHeroSightings.entities.Location;
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
public class LocationDaoDB implements LocationDao {


    @Autowired
    JdbcTemplate jdbc;


    @Override
    public Location getLocationByID(int id) {

        try {
            final String SELECT_LOCATION_BY_ID = "SELECT * FROM location WHERE locationPK = ?";
            return jdbc.queryForObject(SELECT_LOCATION_BY_ID, new LocationMapper(), id);
        } catch(DataAccessException ex) {
            return null;
        }
    }


    @Override
    public List<Location> getAllLocations() {

        final String SELECT_ALL_LOCATIONS = "SELECT * FROM location";
        return jdbc.query(SELECT_ALL_LOCATIONS, new LocationMapper());
    }

    @Override
    public Location addLocation(Location location) {
        final String INSERT_LOCATION = "INSERT INTO location(LocationName, Description, LocationAddress, Latitude, Longitude) VALUES(?,?,?,?,?)";
        jdbc.update(INSERT_LOCATION,
                location.getName(),
                location.getDescription(),
                location.getAddress(),
                location.getLatitude(),
                location.getLongitude());

        // Retrieve the last inserted ID
        String selectLastIdQuery = "SELECT LAST_INSERT_ID()";
        int locationId = jdbc.queryForObject(selectLastIdQuery, Integer.class);

        // Set the location ID
        location.setId(locationId);
        return location;
    }


    @Override
    public void updateLocation(Location location) {

        final String UPDATE_LOCATION = "UPDATE location SET LocationName = ?, Description = ?, LocationAddress = ?, Latitude = ?, Longitude = ? WHERE locationPK = ?";
        jdbc.update(UPDATE_LOCATION,
                location.getName(),
                location.getDescription(),
                location.getAddress(),
                location.getLatitude(),
                location.getLongitude(),
                location.getId());
    }



    @Override
    @Transactional
    public void deleteLocationByID(int id) {
        final String DELETE_LOCATION_BY_SIGHTING = "DELETE FROM sighting WHERE locationPK = ?";
        jdbc.update(DELETE_LOCATION_BY_SIGHTING, id);

        final String DELETE_LOCATION = "DELETE FROM location WHERE locationPK =?";
        jdbc.update(DELETE_LOCATION, id);

    }


    @Override
    public List<Location> getLocationsByHero(Hero hero) {

        final String SELECT_LOCATION_BY_HERO = "SELECT I.* FROM location I INNER JOIN sighting s ON " +
                "s.locationPK = I.locationPK WHERE s.heroPK = ?";
        return jdbc.query(SELECT_LOCATION_BY_HERO, new LocationMapper(), hero.getId());

    }


    public static final class LocationMapper implements RowMapper<Location> {


        @Override
        public Location mapRow(ResultSet rs, int index) throws SQLException {
            Location location = new Location();
            location.setId(rs.getInt("LocationPK"));
            location.setName(rs.getString("LocationName"));
            location.setDescription(rs.getString("Description"));
            location.setAddress(rs.getString("LocationAddress"));
            location.setLatitude(rs.getString("Latitude"));
            location.setLongitude(rs.getString("Longitude"));
            return location;
        }
    }




}
