package com.we.SuperHeroSightings.Controller;


import com.we.SuperHeroSightings.entities.Hero;
import com.we.SuperHeroSightings.entities.Power;
import com.we.SuperHeroSightings.service.ServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PowerController {

    @Autowired
    ServiceInterface service;

    @GetMapping("powers")
    public String displayPowers(Model model) {
        List<Power> powers = service.getAllPowers();
        model.addAttribute("powers", powers);
        return "powers";
    }

    @GetMapping("addPower")
    public String addPower(Integer id, Model model) {
        return "addPower";
    }

    @PostMapping("addPower")
    public String addPower(HttpServletRequest request) {
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        Power power = new Power();
        power.setName(name);
        power.setDescription(description);
        service.addPower(power);

        return "redirect:/powers";
    }

    @PostMapping("deletePower")
    public String deletePower(Integer id) {
        service.deletePowerByID(id);

        return "redirect:/powers";
    }

    @GetMapping("powerDetail")
    public String powerDetail(Integer id, Model model) {
        Power power = service.getPowerByID(id);
        List<Hero> heroes = service.getAllHeros();
        model.addAttribute("heroes", heroes);
        model.addAttribute("power", power);

        return "powerDetail";
    }

    @GetMapping("editPower")
    public String editPower(HttpServletRequest request, Model model) {
        int id = Integer.parseInt(request.getParameter("id"));
        Power power = service.getPowerByID(id);

        model.addAttribute("power", power);

        return "editPower";
    }

    @PostMapping("editPower")
    public String performEditPower(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        Power power = service.getPowerByID(id);

        power.setName(request.getParameter("name"));
        power.setDescription(request.getParameter("description"));

        service.updatePower(power);

        return "redirect:/powers";
    }
}