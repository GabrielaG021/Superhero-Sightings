package com.we.SuperHeroSightings.service;

import com.we.SuperHeroSightings.entities.*;

import java.time.LocalDateTime;
import java.util.List;

public interface ServiceInterface {

    Hero getHeroByID(int id);
    List<Hero> getAllHeros();
    Hero addHero(Hero hero);
    void updateHero(Hero hero);
    void deleteHeroByID(int id);


    List<Hero> getHerosByLocation(Location location);
    List<Hero> getHerosByOrganization(Organization organization);

    void validateHero(Hero hero) throws DuplicateNameExistsException;

    Location getLocationByID(int id);
    List<Location> getAllLocations();
    Location addLocation(Location location);
    void updateLocation(Location location);
    void deleteLocationByID(int id);

    List<Location> getLocationsByHero(Hero hero);

    Organization getOrganizationByID(int id);
    List<Organization> getAllOrganizations();
    Organization addOrganization(Organization organization);
    void updateOrganization(Organization organization);
    void deleteOrganizationByID(int id);

    List<Organization> getOrganizationsByHero(Hero hero);

    void validateOrganization(Organization organization) throws DuplicateNameExistsException, InvalidDataException;

    Power getPowerByID(int id);
    List<Power> getAllPowers();
    Power addPower(Power power);
    void updatePower(Power power);
    void deletePowerByID(int id);

    void validatePower(Power power) throws DuplicateNameExistsException;

    Sighting getSightingByID(int id);
    List<Sighting> getAllSightings();
    Sighting addSighting(Sighting sighting);
    void updateSighting(Sighting sighting);
    void deleteSightingByID(int id);

    List<Sighting> getSightingsByDate(LocalDateTime date);
    List<Sighting> getSightingsByLocation(Location location);
    List<Sighting> getSightingsByHero(Hero hero);

    public void validateLocation(Location location) throws DuplicateNameExistsException, InvalidDataException;
}
