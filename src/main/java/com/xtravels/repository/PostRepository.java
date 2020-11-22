package com.xtravels.repository;

import com.xtravels.models.Post;
import com.xtravels.models.PrivacyType;
import com.xtravels.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByPrivacy(PrivacyType privacyType, Pageable pageable);
    Page<Post> findByUserAndPinned(User user,Boolean pinned, Pageable pageable);
    List<Post> findByPinnedAndUserId(Boolean pinned,Long userId);

    List<Post> findAllByUserId(Long userId);
    Optional<Post> findByUserAndId(User user, Long id);
    Optional<Post> findByUserAndPinned(User user, Boolean pinned);


    @Transactional
    @Modifying
    @Query("update Post p set p.pinned =?2  where p.id=?1")
    void updatePinPost(Long id,Boolean pinned);

    @Transactional
    @Modifying
    @Query("update Post p set p.pinned = false where p.pinned=true and p.user=?1")
    void unPinAllPinnedPost(User user);



}

