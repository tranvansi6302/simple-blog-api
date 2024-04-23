package com.simpleblogapi.simpleblogapi.services;

import com.simpleblogapi.simpleblogapi.dtos.CreatePostDTO;
import com.simpleblogapi.simpleblogapi.dtos.UpdatePostDTO;
import com.simpleblogapi.simpleblogapi.enums.PostStatus;
import com.simpleblogapi.simpleblogapi.exceptions.DataNotFoundException;
import com.simpleblogapi.simpleblogapi.exceptions.PermissionDeniedException;
import com.simpleblogapi.simpleblogapi.models.User;
import com.simpleblogapi.simpleblogapi.responses.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;

public interface IPostService {
    Page<PostResponse> findAll(PostStatus postStatus, PageRequest pageRequest);
    PostResponse updatePost(Long id, UpdatePostDTO updatePostDTO) throws DataNotFoundException, PermissionDeniedException;
    PostResponse uploadImage(Long id, String imageUrl) throws DataNotFoundException, IOException, PermissionDeniedException;
    void deletePost(Long id) throws DataNotFoundException, PermissionDeniedException;
    PostResponse createPost(CreatePostDTO createPostDTO) throws DataNotFoundException;
    PostResponse getPostById(Long id) throws DataNotFoundException;
}
