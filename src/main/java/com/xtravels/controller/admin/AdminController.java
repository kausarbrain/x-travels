package com.xtravels.controller.admin;

import com.xtravels.models.Location;
import com.xtravels.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    LocationService locationService;

    @GetMapping
    public String location(Model model){
        var locations=locationService.getAll();
        model.addAttribute("locationForm",new Location());
        model.addAttribute("locations",locations);
        return "admin/location/index";
    }

    @PostMapping("/location")
    public String location(@Valid @ModelAttribute("locationForm") Location location, BindingResult bindingResult){
        if(!bindingResult.hasErrors()){
            locationService.save(location);
        }
        return "redirect:/admin";
    }

}
