package com.xtravels.controller;

import com.xtravels.models.PrivacyType;
import com.xtravels.service.PostService;
import com.xtravels.service.UserService;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Optional;

@Controller
@RequestMapping({"/", "/home"})
public class HomeController {
    final
    UserService userService;
    final PostService postService;


    private int pageSize = 5;
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public HomeController(UserService userService,PostService postService) {

        this.userService = userService;
        this.postService=postService;
    }



    @GetMapping({"/","/home", "/home/page/{page}"})
    public String index(Model model, @PathVariable(value ="page") Optional<Integer> page){
        int currentPage = page.orElse(1);
        Pageable pageable = PageRequest.of(currentPage-1,
                this.pageSize, Sort.by("id").descending());
        var postPage= postService.findAllPostOnPrivacyType(PrivacyType.PUBLIC,pageable);
        model.addAttribute("postPage",postPage);
        model.addAttribute("currentPage",currentPage);
        System.out.println(postPage);
        return "home/index";
    }
}
