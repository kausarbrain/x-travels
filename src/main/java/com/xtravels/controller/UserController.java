package com.xtravels.controller;

import com.xtravels.auth.LoginUserDetails;
import com.xtravels.models.Post;
import com.xtravels.models.User;
import com.xtravels.service.PostService;
import com.xtravels.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

@Controller
 public class UserController {
    private int pageSize = 5;
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    @Autowired
     public PostService postService;
    @Autowired
    UserService userService;

    @GetMapping({"/profile", "profile/page/{page}"})
    public ModelAndView profile( @PathVariable(value="page") Optional<Integer> page,Authentication authentication){
       ModelAndView mv=new ModelAndView("user/allPost");
        User user =getLoginUser(authentication);
        int currentPage = page.orElse(1);
        Pageable pageable = PageRequest.of(currentPage-1,
                this.pageSize, Sort.by("id").descending());

        var postPage= postService.findAllByUser(user,pageable);
        mv.addObject("postPage",postPage);
        mv.addObject("currentPage",currentPage);
        mv=binPinnedPostToView(mv,user);
        return mv;
    }


    @GetMapping("/user/profile/{id}")
    public ModelAndView profile(@PathVariable(value="id") @Min(1) Long id, Authentication auth){
        var mv=new ModelAndView("user/profile/index");
        Optional<User> user =userService.getUserById(id);
        mv.addObject("user",user);
        User loginUser=getLoginUser(auth);
        if(user.isPresent()){
            mv.addObject("isShowActionDots",loginUser.getId()==user.get().getId());
            mv=binPinnedPostToView(mv,user.get());
            List<Post> posts=postService.findAllPostExceptPinnedPost(id);
            mv.addObject("posts",posts);
        }
        return mv;
    }

    private ModelAndView binPinnedPostToView(ModelAndView mv,User user){
        Optional<Post> pinnedPost=postService.getUserPinnedPost(user);
        mv.addObject("pinnedPost",pinnedPost);
        return mv;
    }

    @GetMapping("/login")
    public String showLogin(Authentication auth){
        if(auth!=null && auth.isAuthenticated()){
            return "redirect:/home";
        }
        return "login/index";
    }
    private User getLoginUser(Authentication authentication){
        var userDetails=(LoginUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }

}
