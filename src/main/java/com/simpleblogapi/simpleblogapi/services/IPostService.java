package com.simpleblogapi.simpleblogapi.services;

import com.simpleblogapi.simpleblogapi.dtos.PostDTO;
import com.simpleblogapi.simpleblogapi.exceptions.DataNotFoundException;
import com.simpleblogapi.simpleblogapi.responses.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;

public interface IPostService {
    Page<PostResponse> findAll(PageRequest pageRequest);
    PostResponse updatePost(Long id, PostDTO postDTO) throws DataNotFoundException;
    PostResponse uploadImage(Long id, String imageUrl) throws DataNotFoundException, IOException;
    void deletePost(Long id) throws DataNotFoundException;
}
