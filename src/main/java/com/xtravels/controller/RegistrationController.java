package com.xtravels.controller;

import com.xtravels.models.dto.UserRegistrationDto;
import com.xtravels.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
public class RegistrationController implements WebMvcConfigurer {

    private final UserService userService;
      PasswordEncoder encoder;

    @Autowired
    public RegistrationController(UserService userService,  PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder=encoder;
    }

    @GetMapping
    public String index(Model model,Authentication auth){
        if(auth!=null && auth.isAuthenticated()){
            return "redirect:/home";
        }
        model.addAttribute("user",new UserRegistrationDto());
        return "registration/index";
    }



    @PostMapping
    public String save(@ModelAttribute("user") @Valid UserRegistrationDto userData,BindingResult bindingResult,Model model) {
        var isSuccess=userService.registerUser(userData,bindingResult,encoder);
        if(!isSuccess){
            model.addAttribute("user", userData);
            return "registration/index";
        }
        return "redirect:/login";

    }
}
