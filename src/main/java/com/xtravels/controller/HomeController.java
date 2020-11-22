package com.xtravels.controller;

import com.xtravels.models.PrivacyType;
import com.xtravels.service.PostService;
import com.xtravels.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Optional;

@Controller
@RequestMapping({"/", "/home"})
public class HomeController {
    private int pageSize = 5;
    final PostService postService;
    public HomeController(PostService postService) {
        this.postService=postService;
    }

    @GetMapping({"/","/home", "/home/page/{page}"})
    public String index(Model model, @PathVariable(value ="page") Optional<Integer> page){
        int currentPage = page.orElse(1);
        var postPage= postService.findAllPostOnPrivacyType(PrivacyType.PUBLIC,currentPage,pageSize);
        model.addAttribute("postPage",postPage);
        model.addAttribute("currentPage",currentPage);
        return "home/index";
    }
}
