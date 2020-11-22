package com.xtravels.controller;

import com.xtravels.auth.LoginUserDetails;
import com.xtravels.models.Location;
import com.xtravels.models.Post;
import com.xtravels.models.PrivacyType;
import com.xtravels.models.User;
import com.xtravels.models.dto.PostDto;
import com.xtravels.service.LocationService;
import com.xtravels.service.PostService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/post")
public class PostController {
    final
    PostService postService;

    final
    LocationService locationService;

    public PostController(PostService postService, LocationService locationService) {
        this.postService = postService;
        this.locationService = locationService;
    }

    @GetMapping
    public ModelAndView showPostForm(){
        ModelAndView mv=new ModelAndView("post/add");
        mv.addObject("post",new PostDto());
        mv=bindDataToModelVew(mv);
        return  mv;

    }

    @PostMapping
    public ModelAndView savePost(@Valid  @ModelAttribute("post") PostDto postDto, BindingResult bindingResult,  Authentication authentication){
        ModelAndView mv=new ModelAndView("post/add");
        if(!bindingResult.hasErrors()){
            Location location=getLocation(postDto.getLocation(),bindingResult);
            postService.save(postDto,location,getLoginUser(authentication));
            mv.setViewName("redirect:/home");
        }else{
            mv=bindDataToModelVew(mv);
        }
        return  mv;

    }

    private ModelAndView bindDataToModelVew(ModelAndView mv){
        List<Location> locations=locationService.getAllLocationOrderByNameAsc();
        mv.addObject("locations",locations);
        mv.addObject("privacyTypes", PrivacyType.values());
        return  mv;
    }


    @GetMapping(value = "update/{id}")
    public ModelAndView editPost( @PathVariable("id") @Min(1) Long id, Authentication authentication){
        var mv=new ModelAndView("post/edit");
        var user=getLoginUser(authentication);
        Optional<Post> existPost=postService.getUserPostById(user,id);
        mv.addObject("existPost",existPost);
        if(existPost.isPresent()){
            mv=addDataToView(mv);
            PostDto postDto=new PostDto();
            BeanUtils.copyProperties(existPost.get(),postDto);
            postDto.setLocation(existPost.get().getLocation().getId());
            mv.addObject("post",postDto);
        }
        return  mv;

    }
    private ModelAndView addDataToView(ModelAndView mv){
        List<Location> locations=locationService.getAllLocationOrderByNameAsc();
        mv.addObject("locations",locations);
        mv.addObject("privacyTypes", PrivacyType.values());
        return  mv;
    }

    @PostMapping(value = "/update/{id}")
    public ModelAndView updatePost(@PathVariable("id") @Min(1) Long id, @Valid  @ModelAttribute("post") PostDto postDto, BindingResult bindingResult, Authentication authentication){
        ModelAndView mv=new ModelAndView("post/edit");
        User user=getLoginUser(authentication);
        Optional<Post> existPost=postService.getUserPostById(user,id);
        if(!bindingResult.hasErrors() && existPost.isPresent()){
            Location location=getLocation(postDto.getLocation(),bindingResult);
            Post post =existPost.get();
            post.setLocation(location);
            post.setDetails(postDto.getDetails());
            post.setPrivacy(postDto.getPrivacy());
            postService.update(post);
            mv.setViewName("redirect:/profile");
        }else{
            mv.addObject("existPost",existPost);
            mv=addDataToView(mv);
        }
        return mv;

    }

    @PostMapping(value = "/pin/{id}")
    public String pin(@PathVariable("id") @Min(1) Long id,  Model model,Authentication authentication){
        User user=getLoginUser(authentication);
        Optional<Post> post= postService.getUserPostById(user,id);
        if(post.isPresent()){

            postService.pinPost(id,user);
        }
        return "redirect:/profile";
    }
    @PostMapping(value = "/unpin/{id}")
    public String unpin(@PathVariable("id") @Min(1) Long id,  Model model,Authentication authentication){
        User user=getLoginUser(authentication);
        Optional<Post> post= postService.getUserPostById(user,id);
        if(post.isPresent()){
            postService.unpinPost(id);
        }
        return "redirect:/profile";
    }

        private Location getLocation(Long id,BindingResult bindingResult){
            Optional<Location> location=  locationService.findById(id);
            if(location.isEmpty()){
                bindingResult.rejectValue("location","error.location","Provided Location is not available");
                return  null;
            }else{
                return  location.get();
            }

    }
        private User getLoginUser(Authentication authentication){
            var userDetails=(LoginUserDetails) authentication.getPrincipal();
            return userDetails.getUser();
        }


}
