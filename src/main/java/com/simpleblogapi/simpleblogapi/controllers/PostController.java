package com.simpleblogapi.simpleblogapi.controllers;

import com.simpleblogapi.simpleblogapi.dtos.PostDTO;
import com.simpleblogapi.simpleblogapi.responses.*;
import com.simpleblogapi.simpleblogapi.services.IPostService;
import com.simpleblogapi.simpleblogapi.utils.Validator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final IPostService postService;

    @GetMapping("")
    public ResponseEntity<?> getAllPosts(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
        Page<PostResponse> postPage = postService.findAll(pageRequest);
        List<PostResponse> posts = postPage.getContent();
        PaginationResponse pagination = new PaginationResponse(page, limit, postPage.getTotalPages());
        ListPostResponse listPostResponse = new ListPostResponse(posts, pagination);
        return ResponseEntity.ok(listPostResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostDTO postDTO,
            BindingResult result
    ) {

        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(Validator.getMessageValidator(result));
            }
            PostResponse postResponse = postService.updatePost(id, postDTO);
            return ResponseEntity.ok(postResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }


    @PatchMapping(value = "/{id}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(
            @PathVariable Long id,
            @ModelAttribute("file") MultipartFile file
    ) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("file", "File is required"));
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            return ResponseEntity.badRequest().body(Collections
                    .singletonMap("file", "File size must be less than 5MB"));
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body(Collections.singletonMap("message", "File must be an image"));
        }
        try {
            String imageUrl = storeImage(file);
            PostResponse postResponse = postService.uploadImage(id, imageUrl);
            return ResponseEntity.ok(postResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections
                    .singletonMap("message", e.getMessage()));
        }
    }

    private String storeImage(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueFileName = System.currentTimeMillis() + "-" + fileName;
        // Noi upload anh
        Path path = Paths.get("uploads");
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        Path des = Paths.get(path + File.separator + uniqueFileName);
        Files.copy(file.getInputStream(), des, StandardCopyOption.REPLACE_EXISTING); // StandardCopyOption.REPLACE_EXISTING ghi de neu ton tai
        return uniqueFileName;
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

}
