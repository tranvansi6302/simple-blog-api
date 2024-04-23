package com.simpleblogapi.simpleblogapi.repositories;

import com.simpleblogapi.simpleblogapi.enums.PostStatus;
import com.simpleblogapi.simpleblogapi.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByPostStatus(PostStatus postStatus, Pageable pageable);


}
