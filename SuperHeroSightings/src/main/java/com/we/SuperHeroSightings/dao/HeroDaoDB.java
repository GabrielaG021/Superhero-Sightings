package com.we.SuperHeroSightings.dao;

//import com.we.SuperHeroSightings.dao.Mappers.OrganizationMapper;
import com.we.SuperHeroSightings.entities.Hero;
import com.we.SuperHeroSightings.entities.Location;
import com.we.SuperHeroSightings.entities.Organization;
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
import com.we.SuperHeroSightings.dao.PowerDaoDB.PowerMapper;
import com.we.SuperHeroSightings.dao.OrganizationDaoDB.OrganizationMapper;

/**
 *
 * @author jtriolo
 */


@Repository
public class HeroDaoDB implements HeroDao {

    @Autowired
    JdbcTemplate jdbc;


    @Override
    public Hero getHeroByID(int id) {
        try {
            String sql = "SELECT * FROM hero WHERE HeroPK = ?";
            Hero hero = jdbc.queryForObject(sql, new HeroMapper(), id);
            hero.setPower(getPowerForHero(id));
            hero.setOrganizations(getOrganizationsForHero(id));
            return hero;
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Transactional
    @Override
    public Hero addHero(Hero hero) {
        String sql = "INSERT INTO hero (HeroName, Type, Description, PowerPK) VALUES (?, ?, ?, ?)";
        jdbc.update(sql, hero.getName(), hero.getType(), hero.getDescription(), hero.getPower().getId());

        int id = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        hero.setId(id);
        addHeroToHeroOrganization(hero);
        return hero;
    }

    private void addHeroToHeroOrganization(Hero hero){
        String sql = "INSERT INTO HeroOrganization (HeroPK, OrganizationPK) VALUES (?, ?)";

        for (Organization org : hero.getOrganizations()){
            jdbc.update(sql, hero.getId(), org.getId());
        }
    }

    @Override
    public List<Hero> getAllHeros() {
        String sql = "SELECT * FROM hero";
        List<Hero> heroes = jdbc.query(sql, new HeroMapper());
        for (Hero hero : heroes) {
            hero.setPower(getPowerForHero(hero.getId()));
            hero.setOrganizations(getOrganizationsForHero(hero.getId()));
        }
        return heroes;
    }

    @Transactional
    @Override
    public void updateHero(Hero hero) {
        String sql = "UPDATE hero SET HeroName = ?, Type = ?, Description = ?, PowerPK = ? WHERE HeroPK = ?";
        jdbc.update(sql, hero.getName(), hero.getType(), hero.getDescription(), hero.getPower().getId(), hero.getId());
        jdbc.update("DELETE FROM heroorganization WHERE HeroPK = ?", hero.getId());
        addHeroToHeroOrganization(hero);
    }

    @Transactional
    @Override
    public void deleteHeroByID(int id) {
        String deleteHeroOrganizationSQL = "DELETE FROM heroorganization WHERE HeroPK = ?";
        jdbc.update(deleteHeroOrganizationSQL, id);
        // First, delete any associated sightings for the hero
        String deleteSightingsSQL = "DELETE FROM sighting WHERE HeroPK = ?";
        jdbc.update(deleteSightingsSQL, id);

        // Then, delete any associated records from the heroorganization table

        //  delete the hero record from the hero table
        String deleteHeroSQL = "DELETE FROM hero WHERE HeroPK = ?";
        jdbc.update(deleteHeroSQL, id);
    }

    public List<Hero> getHerosByLocation(Location location) {
        String sql = "SELECT DISTINCT h.* FROM hero h INNER JOIN sighting s " +
                " ON h.heroPK = s.heroPK WHERE s.locationPK = ?";
        List<Hero> heros = jdbc.query(sql, new HeroMapper(), location.getId());
        for (Hero hero : heros){
            hero.setPower(getPowerForHero(hero.getId()));
            hero.setOrganizations(getOrganizationsForHero(hero.getId()));
        }
        return heros;
    }

    @Override
    public List<Hero> getHerosByOrganization(Organization organization) {
        String sql = "SELECT DISTINCT h.* FROM hero h INNER JOIN heroorganization ho " +
                " ON h.heroPK = ho.heroPK WHERE ho.organizationPK = ?";

        List<Hero> heros = jdbc.query(sql, new HeroMapper(), organization.getId());
        for (Hero hero : heros){
            hero.setPower(getPowerForHero(hero.getId()));
            hero.setOrganizations(getOrganizationsForHero(hero.getId()));
        }
        return heros;
    }

    private List<Organization> getOrganizationsForHero(int heroId) {
        String sql = "SELECT o.* FROM organization o " +
                "INNER JOIN heroorganization ho ON o.OrganizationPK = ho.OrganizationPK " +
                "WHERE ho.HeroPK = ?";
        List<Organization> heroOrganizations = jdbc.query(sql, new OrganizationMapper(), heroId);
        return heroOrganizations;
    }

    private Power getPowerForHero(int heroId) {
        String sql = "SELECT p.* FROM power p " +
                "INNER JOIN hero h ON p.PowerPK = h.PowerPK " +
                "WHERE h.HeroPK = ?";
        try{
            return jdbc.queryForObject(sql, new PowerMapper(), heroId);
        } catch(DataAccessException ex){
            return null;
        }
        //Power heroPower = jdbc.queryForObject(sql, new PowerMapper(), heroId);
        //return heroPower;
    }

    public static final class HeroMapper implements RowMapper<Hero> {
        @Override
        public Hero mapRow(ResultSet rs, int rowNum) throws SQLException {
            Hero hero = new Hero();
            hero.setId(rs.getInt("heroPK"));
            hero.setName(rs.getString("heroName"));
            hero.setDescription(rs.getString("description"));
            hero.setType(rs.getString("type"));
            return hero;
        }
    }
}