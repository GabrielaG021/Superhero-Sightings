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
public class LocationController {

    @Autowired
    ServiceInterface service;

    @GetMapping("locations")
    public String displayLocations(Model model) {
        List<Location> locations = service.getAllLocations();
        model.addAttribute("locations", locations);
        return "locations";
    }
    @GetMapping("addLocation")
    public String addLocation(Model model) {
        return "addLocation";
    }


    @PostMapping("addLocation")
    public String addLocation(HttpServletRequest request) {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String address = request.getParameter("address");
        String longitude = request.getParameter("longitude");
        String latitude = request.getParameter("latitude");

        Location location = new Location();
        location.setName(name);
        location.setDescription(description);
        location.setAddress(address);
        location.setLongitude(longitude);
        location.setLatitude(latitude);
        service.addLocation(location);

        return "redirect:/locations";
    }

    @PostMapping("deleteLocation")
    public String deleteLocation(Integer id) {
        service.deleteLocationByID(id);

        return "redirect:/locations";
    }

    @GetMapping("locationDetail")
    public String locationDetail(Integer id, Model model) {
        Location location = service.getLocationByID(id);
        List<Sighting> sightings = service.getSightingsByLocation(location);
        model.addAttribute("location", location);
        model.addAttribute("sightings", sightings);

        return "locationDetail";
    }

    @GetMapping("editLocation")
    public String editLocation(HttpServletRequest request, Model model) {
        int id = Integer.parseInt(request.getParameter("id"));
        Location location = service.getLocationByID(id);
        model.addAttribute("location", location);

        return "editLocation";
    }

    @PostMapping("editLocation")
    public String performEditSighting(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        Location location = service.getLocationByID(id);
        location.setName(request.getParameter("name"));
        location.setDescription(request.getParameter("description"));
        location.setAddress(request.getParameter("address"));
        location.setLongitude(request.getParameter("longitude"));
        location.setLatitude(request.getParameter("latitude"));

        service.updateLocation(location);

        return "redirect:/locations";
    }
}