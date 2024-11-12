package com.capstone.unwind.controller;

import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.BlogDTO.BlogPostRequestDto;
import com.capstone.unwind.model.BlogDTO.BlogPostResponseDto;
import com.capstone.unwind.model.BlogDTO.ListBlogPostDto;
import com.capstone.unwind.model.BlogDTO.UpdateBlogPostRequestDto;
import com.capstone.unwind.service.ServiceInterface.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/system-staff/blog")
@RequiredArgsConstructor
@CrossOrigin
public class BlogPostController {
    @Autowired
    private BlogPostService blogPostService;

    @PostMapping
    public ResponseEntity<BlogPostResponseDto> createBlogPost(@RequestBody BlogPostRequestDto blogPostDTO) {
        BlogPostResponseDto createdPost = blogPostService.createBlogPost(blogPostDTO);
        return new ResponseEntity<>(createdPost, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<ListBlogPostDto>> getAllBlogPosts(
            @RequestParam(required = false, defaultValue = "") String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ListBlogPostDto> blogPosts = blogPostService.getAllBlogPosts(title, pageable);
        return ResponseEntity.ok(blogPosts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogPostResponseDto> updateBlogPost(
            @PathVariable Integer id,
            @RequestBody UpdateBlogPostRequestDto updatedPostDTO) throws OptionalNotFoundException {
            BlogPostResponseDto updatedPost = blogPostService.updateBlogPost(id, updatedPostDTO);
            return ResponseEntity.ok(updatedPost);

    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<BlogPostResponseDto> deActiveBlogPost(@PathVariable Integer id) throws OptionalNotFoundException {

            BlogPostResponseDto deactivatedPost = blogPostService.deActiveBlogPost(id);
            return ResponseEntity.ok(deactivatedPost);
    }

    @GetMapping("/{postingId}")
    public ResponseEntity<BlogPostResponseDto> getBlogPostingDetailById(@PathVariable Integer postingId) throws OptionalNotFoundException {

            BlogPostResponseDto blogPostDetail = blogPostService.getBlogPostingDetailById(postingId);
            return ResponseEntity.ok(blogPostDetail);
    }
}
