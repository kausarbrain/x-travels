package com.xtravels.controller;

import com.xtravels.models.PrivacyType;
import com.xtravels.models.User;
import com.xtravels.models.dto.PostDto;
import com.xtravels.service.LocationService;
import com.xtravels.service.PostService;
import com.xtravels.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Min;


@Controller
@RequestMapping("/post")
public class PostController {
    final PostService postService;

    final LocationService locationService;

    final UserService userService;

    public PostController(PostService postService, LocationService locationService, UserService userService) {
        this.postService = postService;
        this.locationService = locationService;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView showPostForm() {
        ModelAndView mv = new ModelAndView("post/add");
        mv.addObject("post", new PostDto());
        mv.addObject("privacyTypes", PrivacyType.values());
        mv.addObject("locations", locationService.getAllLocationOrderByNameAsc());
        return mv;
    }

    @PostMapping
    public ModelAndView savePost(@Valid @ModelAttribute("post") PostDto postDto, BindingResult bindingResult, Authentication authentication) {
        ModelAndView mv = new ModelAndView("post/add");
        var user = userService.getLoginUser(authentication);
        var isSave = postService.save(postDto,locationService,user,bindingResult);
        if (isSave) {
            mv.setViewName("redirect:/home");
        } else {
           addDataToView(mv);
        }
        return mv;

    }


    @GetMapping(value = "update/{id}")
    public ModelAndView editPost(@PathVariable("id") @Min(1) Long id, Authentication authentication) {
        var mv = new ModelAndView("post/edit");
        var user = userService.getLoginUser(authentication);
        var existPost = postService.getUserPostById(user, id);
        mv.addObject("existPost", existPost);
        if (existPost.isPresent()) {
            mv=addDataToView(mv);
            PostDto postDto = new PostDto();
            BeanUtils.copyProperties(existPost.get(), postDto);
            postDto.setLocation(existPost.get().getLocation().getId());
            mv.addObject("post", postDto);
        }
        return mv;

    }



    @PostMapping(value = "/update/{id}")
    public ModelAndView updatePost(@PathVariable("id") @Min(1) Long id, @Valid @ModelAttribute("post") PostDto postDto, BindingResult bindingResult, Authentication authentication) {
        var  mv = new ModelAndView("post/edit");
        var user = userService.getLoginUser(authentication);
        var existPost = postService.getUserPostById(user, id);
        var  isUpdated = postService.update(postDto, locationService, bindingResult, existPost);
        if (isUpdated) {
            mv.setViewName("redirect:/profile");
        } else {
            mv.addObject("existPost", existPost);
            mv = addDataToView(mv);
        }
        return mv;

    }


    @PostMapping(value = "/pin/{id}")
    public String pin(@PathVariable("id") @Min(1) Long id, Model model, Authentication authentication) {
        User user = userService.getLoginUser(authentication);
        postService.pinPost(id,user);
        return "redirect:/profile";
    }

    @PostMapping(value = "/unpin/{id}")
    public String unpin(@PathVariable("id") @Min(1) Long id, Model model, Authentication authentication) {
        var user = userService.getLoginUser(authentication);
        postService.unpinPost(id,user);
        return "redirect:/profile";
    }

    private ModelAndView addDataToView(ModelAndView mv) {
        mv.addObject("locations", locationService.getAllLocationOrderByNameAsc());
        mv.addObject("privacyTypes", PrivacyType.values());
        return mv;
    }


}
