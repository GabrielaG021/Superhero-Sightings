package com.we.SuperHeroSightings.Controller;

import com.we.SuperHeroSightings.entities.Hero;
import com.we.SuperHeroSightings.entities.Location;
import com.we.SuperHeroSightings.entities.Sighting;
import com.we.SuperHeroSightings.service.ServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SightingController {

    @Autowired
    ServiceInterface service;

    @GetMapping("sightings")
    public String displaySightings(Model model) {
        List<Sighting> sightings = service.getAllSightings();
        List<Hero> heroes = service.getAllHeros();
        List<Location> locations = service.getAllLocations();
        model.addAttribute("sightings", sightings);
        model.addAttribute("heroes", heroes);
        model.addAttribute("locations", locations);

        return "sightings";
    }

    @GetMapping("addSighting")
    public String addSighting(Model model) {
        List<Hero> heroes = service.getAllHeros();
        List<Location> locations = service.getAllLocations();
        model.addAttribute("locations", locations);
        model.addAttribute("heroes", heroes);
        model.addAttribute("sighting", new Sighting());
        return "addSighting";
    }


    @PostMapping("addSighting")
    public String addSighting(Sighting sighting, HttpServletRequest request) {
        String heroId = request.getParameter("hero");
        String locationId = request.getParameter("location");

        sighting.setHero(service.getHeroByID(Integer.parseInt(heroId)));
        sighting.setLocation(service.getLocationByID(Integer.parseInt(locationId)));


        service.addSighting(sighting);

        return "redirect:/sightings";
    }

    @PostMapping("deleteSighting")
    public String deleteSighting(Integer id) {
        service.deleteSightingByID(id);

        return "redirect:/sightings";
    }

    @GetMapping("sightingDetail")
    public String sightingDetail(Integer id, Model model) {
        Sighting sighting = service.getSightingByID(id);
        model.addAttribute("sighting", sighting);

        return "sightingDetail";
    }

    @GetMapping("editSighting")
    public String editSighting(Integer id, Model model) {
        Sighting sighting = service.getSightingByID(id);
        List<Hero> heroes = service.getAllHeros();
        List<Location> locations = service.getAllLocations();
        model.addAttribute("sighting", sighting);
        model.addAttribute("heroes", heroes);
        model.addAttribute("locations", locations);

        return "editSighting";
    }

    @PostMapping("editSighting")
    public String performEditSighting(Sighting sighting, HttpServletRequest request) {
        String heroId = request.getParameter("heroPK");
        String locationId = request.getParameter("locationPK");

        sighting.setHero(service.getHeroByID(Integer.parseInt(heroId)));
        sighting.setLocation(service.getLocationByID(Integer.parseInt(locationId)));

        service.updateSighting(sighting);

        return "redirect:/sightings";
    }




}
