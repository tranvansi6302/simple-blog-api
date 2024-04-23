package com.simpleblogapi.simpleblogapi.services;

import com.simpleblogapi.simpleblogapi.components.JwtTokenUtil;
import com.simpleblogapi.simpleblogapi.dtos.CreatePostDTO;
import com.simpleblogapi.simpleblogapi.dtos.UpdatePostDTO;
import com.simpleblogapi.simpleblogapi.enums.PostStatus;
import com.simpleblogapi.simpleblogapi.exceptions.DataNotFoundException;
import com.simpleblogapi.simpleblogapi.exceptions.PermissionDeniedException;
import com.simpleblogapi.simpleblogapi.models.Category;
import com.simpleblogapi.simpleblogapi.models.Post;
import com.simpleblogapi.simpleblogapi.models.User;
import com.simpleblogapi.simpleblogapi.repositories.CategoryRepository;
import com.simpleblogapi.simpleblogapi.repositories.PostRepository;
import com.simpleblogapi.simpleblogapi.repositories.UserRepository;
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
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public Page<PostResponse> findAll(PostStatus postStatus, PageRequest pageRequest) {
            if (postStatus != null) {
                return postRepository.findAllByPostStatus(postStatus, pageRequest).map(PostResponse::fromPost);
            } else {
                return postRepository.findAll(pageRequest).map(PostResponse::fromPost);
            }
    }


    @Override
    public PostResponse updatePost(Long id, UpdatePostDTO updatePostDTO) throws DataNotFoundException, PermissionDeniedException {

        String roleName = JwtTokenUtil.getCurrentUserRole();
        Long userId = JwtTokenUtil.getUserIdFormToken();
        // Checking can only update my posts if role is user
        if (roleName.equals("ROLE_USER")) {
            Post post = postRepository.findById(id).orElseThrow(
                    () -> new DataNotFoundException("Post not found with id: " + id)
            );
            if (!Objects.equals(post.getUser().getUserId(), userId)) {
                throw new PermissionDeniedException("You can not update this post");
            }
        }

        Post post = postRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Post not found with id: " + id)
        );

        // Check if category id is not null and category exists
        if (updatePostDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(updatePostDTO.getCategoryId()).orElseThrow(
                    () -> new DataNotFoundException("Category not found with id: " + updatePostDTO.getCategoryId())
            );
            post.setCategory(category);
        }

        // Copy properties from postDTO to post except null properties
        BeanUtils.copyProperties(updatePostDTO, post, CheckCondition.getNullPropertyNames(updatePostDTO));
        return PostResponse.fromPost(postRepository.save(post));

    }

    @Override
    public PostResponse uploadImage(Long id, String imageUrl) throws DataNotFoundException, IOException, PermissionDeniedException {
        String roleName = JwtTokenUtil.getCurrentUserRole();
        Long userId = JwtTokenUtil.getUserIdFormToken();
        // Checking can only update my posts if role is user
        if (roleName.equals("ROLE_USER")) {
            Post post = postRepository.findById(id).orElseThrow(
                    () -> new DataNotFoundException("Post not found with id: " + id)
            );
            if (!Objects.equals(post.getUser().getUserId(), userId)) {
                throw new PermissionDeniedException("You can not update this post");
            }
        }
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
    public void deletePost(Long id) throws DataNotFoundException, PermissionDeniedException {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Post not found with id: " + id)
        );
        String roleName = JwtTokenUtil.getCurrentUserRole();
        Long userId = JwtTokenUtil.getUserIdFormToken();
        // Checking can only delete my posts if rol
        if (roleName.equals("ROLE_USER") && !Objects.equals(post.getUser().getUserId(), userId)) {
            throw new PermissionDeniedException("You can not delete this post");
        }
        postRepository.delete(post);
    }

    @Override
    public PostResponse createPost(CreatePostDTO createPostDTO) throws DataNotFoundException {
        Long userId = JwtTokenUtil.getUserIdFormToken();
        Category category = categoryRepository.findById(createPostDTO.getCategoryId()).orElseThrow(
                () -> new DataNotFoundException("Category not found with id: " + createPostDTO.getCategoryId())
        );
        User user = userRepository.findById(userId).orElseThrow(
                () -> new DataNotFoundException("User not found with id: " + userId)
        );
        Post newPost = Post.builder()
                .postTitle(createPostDTO.getPostTitle())
                .postContent(createPostDTO.getPostContent())
                .category(category)
                .postStatus(PostStatus.UNAPPROVED)
                .postThumbnail("")
                .user(user)
                .build();
        return PostResponse.fromPost(postRepository.save(newPost));
    }

    @Override
    public PostResponse getPostById(Long id) throws DataNotFoundException {
        return PostResponse.fromPost(postRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Post not found with id: " + id)
        ));
    }
}
