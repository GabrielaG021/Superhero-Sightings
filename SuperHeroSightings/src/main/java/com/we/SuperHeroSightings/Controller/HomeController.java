package com.we.SuperHeroSightings.Controller;

import com.we.SuperHeroSightings.entities.Sighting;
import com.we.SuperHeroSightings.service.ServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class HomeController {

    @Autowired
    ServiceInterface service;

    @GetMapping("/")
    public String index(Model model) {
        List<Sighting> sightings = service.getAllSightings();
        if (sightings.size() > 10) {
            sightings = sightings.subList(0, 10);
        }
        model.addAttribute("sightings", sightings);

        return "index";
    }


}
