
package com.we.SuperHeroSightings.dao;


import com.we.SuperHeroSightings.entities.Hero;
import com.we.SuperHeroSightings.entities.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.we.SuperHeroSightings.dao.HeroDaoDB.HeroMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Gabriela Gutierrez
 */
@Repository
public class OrganizationDaoDB implements OrganizationDao {
    
    @Autowired
    JdbcTemplate jdbc;

    final String GET_ORGANIZATION_BY_ID = "SELECT * FROM organization WHERE organizationPK = ?";
    final String GET_ALL_ORGANIZATIONS = "SELECT * FROM organization";
    final String GET_HEROS_BY_ORGANIZATION = "SELECT DISTINCT h.* FROM hero h INNER JOIN heroorganization ho " +
            " ON h.heroPK = ho.heroPK WHERE organizationPK = ?";
    final String INSERT_ORGANIZATION = "INSERT INTO organization (organizationName, type, description, " +
            " organizationAddress, phone, contactInfo) VALUES(?, ?, ?, ?, ?, ?)";
    final String GET_INSERT_ID = "SELECT LAST_INSERT_ID()";
    final String UPDATE_ORGANIZATION = "UPDATE organization SET organizationName = ?, type = ?, description = ?, " +
            " organizationAddress = ?, phone = ?, contactInfo = ? WHERE organizationPK = ?;";
    final String DELETE_HERO_ORGANIZATION = "DELETE FROM heroorganization WHERE organizationPK = ?";
    final String DELETE_ORGANIZATION = "DELETE FROM organization WHERE organizationPK = ?";
    final String GET_ORGANIZATIONS_BY_HERO = "SELECT o.* FROM organization o INNER JOIN heroorganization ho ON " +
            " o.organizationPK = ho.organizationPK WHERE ho.heroPK = ?";

    @Override
    public Organization getOrganizationByID(int id) {
        try{
            Organization organization = jdbc.queryForObject(GET_ORGANIZATION_BY_ID, new OrganizationMapper(), id);
            organization.setMembers(getHeroesByOrganization(organization));
            return organization;
        } catch (DataAccessException e) {
            return null;
        }
    }

    private List<Hero> getHeroesByOrganization(Organization organization) {
        return jdbc.query(GET_HEROS_BY_ORGANIZATION, new HeroMapper(), organization.getId());
    }

    @Override
    public List<Organization> getAllOrganizations() {
        List<Organization> organizations = jdbc.query(GET_ALL_ORGANIZATIONS, new OrganizationMapper());
        for (Organization organization : organizations){
            organization.setMembers(getHeroesByOrganization(organization));
        }
        return organizations;
    }

    @Override
    public Organization addOrganization(Organization organization) {
        jdbc.update(
                INSERT_ORGANIZATION,
                organization.getName(),
                organization.getType(),
                organization.getDescription(),
                organization.getAddress(),
                organization.getPhone(),
                organization.getContact());

        int newId = jdbc.queryForObject(GET_INSERT_ID , Integer.class);
        organization.setId(newId);
        return organization;
    }

    @Override
    public void updateOrganization(Organization organization) {
        jdbc.update(
                UPDATE_ORGANIZATION,
                organization.getName(),
                organization.getType(),
                organization.getDescription(),
                organization.getAddress(),
                organization.getPhone(),
                organization.getContact(),
                organization.getId()
        );
    }

    @Override
    @Transactional
    public void deleteOrganizationByID(int id) {
        jdbc.update(DELETE_HERO_ORGANIZATION, id);
        jdbc.update(DELETE_ORGANIZATION, id);
    }

    @Override
    public List<Organization> getOrganizationsByHero(Hero hero) {
        List<Organization> organizations = jdbc.query(GET_ORGANIZATIONS_BY_HERO, new OrganizationMapper(), hero.getId());
        for (Organization organization : organizations) {
            organization.setMembers(getHeroesByOrganization(organization));
        }
        return organizations;
    }

    public static final class OrganizationMapper implements RowMapper<Organization> {

        @Override
        public Organization mapRow(ResultSet rs, int rowNum) throws SQLException {
            Organization organization = new Organization();
            organization.setId(rs.getInt("OrganizationPK"));
            organization.setName(rs.getString("OrganizationName"));
            organization.setType(rs.getString("Type"));
            organization.setDescription(rs.getString("Description"));
            organization.setAddress(rs.getString("OrganizationAddress"));
            organization.setPhone(rs.getString("Phone"));
            organization.setContact(rs.getString("ContactInfo"));

            return organization;
        }
    }

}
