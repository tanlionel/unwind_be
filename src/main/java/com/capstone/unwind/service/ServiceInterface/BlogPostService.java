package com.capstone.unwind.service.ServiceInterface;

import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.BlogDTO.BlogPostRequestDto;
import com.capstone.unwind.model.BlogDTO.BlogPostResponseDto;
import com.capstone.unwind.model.BlogDTO.ListBlogPostDto;
import com.capstone.unwind.model.BlogDTO.UpdateBlogPostRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogPostService {
    BlogPostResponseDto createBlogPost(BlogPostRequestDto blogPostDTO);
    Page<ListBlogPostDto> getAllBlogPosts(String title, Pageable pageable);
    BlogPostResponseDto updateBlogPost(Integer id, UpdateBlogPostRequestDto updatedPostDTO) throws  OptionalNotFoundException;

    BlogPostResponseDto deActiveBlogPost(Integer id) throws OptionalNotFoundException;
    BlogPostResponseDto getBlogPostingDetailById(Integer postingId) throws OptionalNotFoundException;

}