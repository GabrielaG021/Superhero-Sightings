package com.we.SuperHeroSightings.service;

import com.we.SuperHeroSightings.dao.HeroDao;
import com.we.SuperHeroSightings.dao.LocationDao;
import com.we.SuperHeroSightings.dao.OrganizationDao;
import com.we.SuperHeroSightings.dao.PowerDao;
import com.we.SuperHeroSightings.dao.SightingDao;
import com.we.SuperHeroSightings.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class ServiceLayer implements ServiceInterface {

    @Autowired
    HeroDao heroDao;

    @Autowired
    OrganizationDao organizationDao;

    @Autowired
    LocationDao locationDao;

    @Autowired
    PowerDao powerDao;

    @Autowired
    SightingDao sightingDao;


    @Override
    public Hero getHeroByID(int id) {
        return heroDao.getHeroByID(id);
    }

    @Override
    public List<Hero> getAllHeros() {
       return heroDao.getAllHeros();
    }

    @Override
    public Hero addHero(Hero hero) {
        return heroDao.addHero(hero);
    }

    @Override
    public void updateHero(Hero hero) {
        heroDao.updateHero(hero);
    }

    @Override
    public void deleteHeroByID(int id) {
        heroDao.deleteHeroByID(id);
    }

    @Override
    public List<Hero> getHerosByLocation(Location location) {
        return heroDao.getHerosByLocation(location);
    }

    @Override
    public List<Hero> getHerosByOrganization(Organization organization) {

        return heroDao.getHerosByOrganization(organization);
    }
    @Override
public void validateHero(Hero hero) throws DuplicateNameExistsException {
        List<Hero> heroes =heroDao.getAllHeros();
        boolean isDuplicate =false;
        for(Hero ahero: heroes) {
            if (ahero.getName().toLowerCase().equals(hero.getName().toLowerCase()))
                isDuplicate = true;
        }
        if(isDuplicate) {
            throw new DuplicateNameExistsException("Hero/Villain Name Already Exists in System");

        }
    }
    @Override
    public Location getLocationByID(int id) {
        return locationDao.getLocationByID(id);
    }

    @Override
    public List<Location> getAllLocations() {
        return locationDao.getAllLocations();
    }

    @Override
    public Location addLocation(Location location) {

        return locationDao.addLocation(location);
    }

    @Override
    public void updateLocation(Location location) {
        locationDao.updateLocation(location);

    }

    @Override
    public void deleteLocationByID(int id) {
        locationDao.deleteLocationByID(id);

    }

    @Override
    public List<Location> getLocationsByHero(Hero hero) {
        return locationDao.getLocationsByHero(hero);
    }

    private static class LocationValidator {

        // Method to check if the latitude is valid
        public static boolean isValidLatitude(String latitude) {
            try {
                double lat = Double.parseDouble(latitude);
                return lat >= -90 && lat <= 90;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        // Method to check if the longitude is valid
        public static boolean isValidLongitude(String longitude) {
            try {
                double lon = Double.parseDouble(longitude);
                return lon >= -180 && lon <= 180;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }

    @Override
    public void validateLocation(Location location) throws DuplicateNameExistsException, InvalidDataException {
        List<Location> locations = locationDao.getAllLocations();
        boolean isDupe = false;

        for(Location alocation : locations) {
            if (alocation.getName().toLowerCase().equals(location.getName().toLowerCase())) {
                isDupe = true;
            }
        }
        if(isDupe) {
            throw new DuplicateNameExistsException("Location Name Already Exists");
        }

        // Validate latitude and longitude
        if (!LocationValidator.isValidLatitude(location.getLatitude())) {
            throw new InvalidDataException("Invalid Latitude Format");
        }

        if (!LocationValidator.isValidLongitude(location.getLongitude())) {
            throw new InvalidDataException("Invalid Longitude Format");
        }

    }


    @Override
    public Organization getOrganizationByID(int id) {
        return organizationDao.getOrganizationByID(id);
    }

    @Override
    public List<Organization> getAllOrganizations() {
        return organizationDao.getAllOrganizations();
    }

    @Override
    public Organization addOrganization(Organization organization) {
        return organizationDao.addOrganization(organization);
    }

    @Override
    public void updateOrganization(Organization organization) {
        organizationDao.updateOrganization(organization);
    }

    @Override
    public void deleteOrganizationByID(int id) {
        organizationDao.deleteOrganizationByID(id);
    }

    @Override
    public List<Organization> getOrganizationsByHero(Hero hero) {

        return organizationDao.getOrganizationsByHero(hero);
    }

    @Override
    public void validateOrganization(Organization organization) throws DuplicateNameExistsException, InvalidDataException {
        List<Organization> organizations = organizationDao.getAllOrganizations();
        Boolean isDuplicate = false;
        String phonePattern = "\\d{3}-\\d{3}-d{4}";
        String phone = organization.getPhone();

        for(Organization singleOrganization : organizations) {
            if(singleOrganization.getName().toLowerCase().equals(organization.getName().toLowerCase())) {
                isDuplicate = true;
            }
        }

        if (isDuplicate) {
            throw new DuplicateNameExistsException("The organization name already exists in the system");
        }

        if(phone == null || !phone.matches(phonePattern)) {
            throw new InvalidDataException("Invalid phone format. Please enter valid phone number(Format: ###-###-####");
        }
    }

    @Override
    public Power getPowerByID(int id) {
        return powerDao.getPowerByID(id);
    }

    @Override
    public List<Power> getAllPowers() {
        return powerDao.getAllPowers();
    }

    @Override
    public Power addPower(Power power) {
        return powerDao.addPower(power);
    }

    @Override
    public void updatePower(Power power) {
        powerDao.updatePower(power);
    }

    @Override
    public void deletePowerByID(int id) {
        powerDao.deletePowerByID(id);
    }

    @Override
    public void validatePower(Power power) throws DuplicateNameExistsException {
        List<Power> powers = powerDao.getAllPowers();
        Boolean isDuplicate = false;

        for(Power singlePower : powers) {
            if(singlePower.getName().toLowerCase().equals(power.getName().toLowerCase())) {
                isDuplicate = true;
            }
        }

        if(isDuplicate) {
            throw new DuplicateNameExistsException("The power name already exists in the system.");
        }
    }

    @Override
    public Sighting getSightingByID(int id) {
        return sightingDao.getSightingByID(id);
    }

    @Override
    public List<Sighting> getAllSightings() {
        return sightingDao.getAllSightings();
    }

    @Override
    public Sighting addSighting(Sighting sighting) {
        return sightingDao.addSighting(sighting);
    }

    @Override
    public void updateSighting(Sighting sighting) {
        sightingDao.updateSighting(sighting);
    }

    @Override
    public void deleteSightingByID(int id) {
        sightingDao.deleteSightingByID(id);
    }

    @Override
    public List<Sighting> getSightingsByDate(LocalDateTime date) {
        return sightingDao.getSightingsByDate(date);
    }

    @Override
    public List<Sighting> getSightingsByLocation(Location location) {
            return sightingDao.getSightingsByLocation(location);
    }

    @Override
    public List<Sighting> getSightingsByHero(Hero hero) {
        return sightingDao.getSightingsByHero(hero);
    }
}
