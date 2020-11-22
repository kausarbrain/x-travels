package com.xtravels.controller;

import com.xtravels.models.User;
import com.xtravels.models.dto.UserRegistrationDto;
import com.xtravels.service.UserService;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.Valid;
import java.util.Optional;

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
    public String save(@ModelAttribute("user") @Valid UserRegistrationDto user,BindingResult bindingResult,Model model) {



        if(!user.getPassword().equals(user.getConfirmPassword()))
            bindingResult.rejectValue("confirmPassword","error.user","Confirm Password is not Matching");
      if(!bindingResult.hasErrors()){
          Optional<User> existUser=this.userService.getUserByEmail(user.getEmail());
          if(existUser.isPresent())
              bindingResult.rejectValue("email","error.user","Email is already registerd");
      }

        if(bindingResult.hasErrors()){
            model.addAttribute("user", user);
            return "registration/index";
        }
        user.setPassword(encoder.encode(user.getPassword()));

         this.userService.save(user);
        return "redirect:/login";

    }
}
