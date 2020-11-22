package com.xtravels.service;

import com.xtravels.auth.LoginUserDetails;
import com.xtravels.models.Location;
import com.xtravels.models.Post;
import com.xtravels.models.PrivacyType;
import com.xtravels.models.User;
import com.xtravels.models.dto.PostDto;
import com.xtravels.repository.PostRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;


    public Boolean save(PostDto postDto, LocationService locationService, User user,BindingResult bindingResult) {
        Location location = locationService.getLocation(postDto.getLocation(), bindingResult);
        if (!bindingResult.hasErrors()) {
            Post post = new Post(postDto.getDetails(), postDto.getPrivacy(), location, user);
            post = postRepository.save(post);
            Optional<Post> opPost = Optional.of(post);
            return opPost.isPresent();
        }
        return  false;

    }

    public Page<Post> findAllByUser(User user,int currentPage, int pageSize) {
        Pageable pageable = PageRequest.of(currentPage - 1,
                pageSize, Sort.by("id").descending());
        return postRepository.findByUserAndPinned(user, false, pageable);
    }

    public List<Post> findAllByUserId(Long userId) {
        return postRepository.findAllByUserId(userId);
    }

    public List<Post> findAllPostExceptPinnedPost(Long userId) {
        return postRepository.findByPinnedAndUserId(false, userId);
    }

    public Page<Post> findAllPostOnPrivacyType(PrivacyType privacyType, int currentPage, int pageSize) {
        Pageable pageable = PageRequest.of(currentPage - 1,
                pageSize, Sort.by("id").descending());
        return postRepository.findByPrivacy(PrivacyType.PUBLIC, pageable);
    }


    public Optional<Post> getUserPostById(User user, Long id) {
        return postRepository.findByUserAndId(user, id);
    }

    public Optional<Post> getUserPinnedPost(User user) {
        return postRepository.findByUserAndPinned(user, true);
    }

    public Boolean update(PostDto postDto, LocationService locationService, BindingResult bindingResult, Optional<Post> existPost) {
        var location=locationService.getLocation(postDto.getLocation(),bindingResult);
        if (!bindingResult.hasErrors()) {
            Post post = existPost.get();
            post.setLocation(location);
            post.setDetails(postDto.getDetails());
            post.setPrivacy(postDto.getPrivacy());
            return Optional.of(postRepository.save(post)).isPresent();
        }
        return false;

    }


    @Transactional
    public Boolean pinPost(Long id, User user) {
        var post = getUserPostById(user, id);
        if (post.isPresent()) {
            postRepository.unPinAllPinnedPost(user);
            postRepository.updatePinPost(id, true);
            return true;
        }
        return  false;
    }

    public Boolean unpinPost(Long id,User user) {
        Optional<Post> post = getUserPostById(user, id);
        if (post.isPresent()) {
             postRepository.updatePinPost(id, false);
             return true;
        }
        return false;
    }


}
