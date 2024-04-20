package com.simpleblogapi.simpleblogapi.services;

import com.simpleblogapi.simpleblogapi.dtos.PostDTO;
import com.simpleblogapi.simpleblogapi.enums.PostStatus;
import com.simpleblogapi.simpleblogapi.exceptions.DataNotFoundException;
import com.simpleblogapi.simpleblogapi.models.Category;
import com.simpleblogapi.simpleblogapi.models.Post;
import com.simpleblogapi.simpleblogapi.repositories.CategoryRepository;
import com.simpleblogapi.simpleblogapi.repositories.PostRepository;
import com.simpleblogapi.simpleblogapi.responses.PostResponse;
import com.simpleblogapi.simpleblogapi.utils.CheckCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService{
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public Page<PostResponse> findAll(PageRequest pageRequest) {
        return postRepository.findAll(pageRequest).map(PostResponse::fromPost);
    }

    @Override
    public PostResponse updatePost(Long id, PostDTO postDTO) throws DataNotFoundException {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Post not found with id: " + id)
        );

        // Check if category id is not null and category exists
        if(postDTO.getCategoryId() != null){
            Category category = categoryRepository.findById(postDTO.getCategoryId()).orElseThrow(
                    () -> new DataNotFoundException("Category not found with id: " + postDTO.getCategoryId())
            );
            post.setCategory(category);
        }

        // Copy properties from postDTO to post except null properties
        BeanUtils.copyProperties(postDTO, post, CheckCondition.getNullPropertyNames(postDTO));
        return PostResponse.fromPost(postRepository.save(post));

    }

    @Override
    public PostResponse uploadImage(Long id, String imageUrl) throws DataNotFoundException, IOException {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Post not found with id: " + id)
        );
        // Remove old image if exists
        Path oldImagePath = Paths.get("uploads", post.getPostThumbnail());
        Files.deleteIfExists(oldImagePath);
        post.setPostThumbnail(imageUrl);
        return PostResponse.fromPost(postRepository.save(post));
    }

    @Override
    public void deletePost(Long id) throws DataNotFoundException {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Post not found with id: " + id)
        );
        postRepository.delete(post);
    }
}
