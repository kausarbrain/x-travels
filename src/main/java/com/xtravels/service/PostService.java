package com.xtravels.service;

import com.xtravels.models.Location;
import com.xtravels.models.Post;
import com.xtravels.models.PrivacyType;
import com.xtravels.models.User;
import com.xtravels.models.dto.PostDto;
import com.xtravels.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;

    public Post save(PostDto postDto, Location location,User user) {
        return postRepository.save(new Post(postDto.getDetails(),postDto.getPrivacy(),location,user));
    }

    public Page<Post> findAllByUser(User user, Pageable pageable) {
        return postRepository.findByUserAndPinned(user,false,pageable);
    }

    public List<Post> findAllByUserId(Long userId) {
        return postRepository.findAllByUserId(userId);
    }

    public List<Post> findAllPostExceptPinnedPost(Long userId) {
        return postRepository.findByPinnedAndUserId(false,userId);
    }

    public Page<Post> findAllPostOnPrivacyType(PrivacyType privacyType, Pageable pageable){
        return postRepository.findByPrivacy(PrivacyType.PUBLIC,pageable);
    }

    public Optional<Post> getUserPostById(User user,Long id){
       return postRepository.findByUserAndId(user,id);
    }

    public Optional<Post> getUserPinnedPost(User user){
        return postRepository.findByUserAndPinned(user,true);
    }

    public Post update(Post post){
        return  postRepository.save(post);
    }


    @Transactional
    public void pinPost(Long id,User user){
        postRepository.unPinAllPinnedPost(user);
        postRepository.updatePinPost(id,true);
    }

    public void unpinPost(Long id){
        postRepository.updatePinPost(id,false);
    }


}
