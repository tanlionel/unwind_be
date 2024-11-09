package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.BlogPost;
import com.capstone.unwind.entity.RentalPosting;
import com.capstone.unwind.enums.RentalPostingEnum;
import com.capstone.unwind.exception.EntityDoesNotExistException;
import com.capstone.unwind.exception.ErrMessageException;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.BlogDTO.*;
import com.capstone.unwind.model.PostingDTO.PostingDetailResponseDTO;
import com.capstone.unwind.model.PostingDTO.PostingResponseDTO;
import com.capstone.unwind.repository.BlogPostRepository;
import com.capstone.unwind.service.ServiceInterface.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogPostServiceImplement implements BlogPostService {
    @Autowired
    private BlogPostRepository blogPostRepository;
    @Autowired
    private BlogPostMapper blogPostMapper;
    @Autowired
    private ListBlogPostMapper listblogPostMapper;
    @Override
    public BlogPostResponseDto createBlogPost(BlogPostRequestDto blogPostDTO) {
        BlogPost blogPost = BlogPost.builder()
                .title(blogPostDTO.getTitle())
                .content(blogPostDTO.getContent())
                .author(blogPostDTO.getAuthor())
                .isActive(true)
                .build();
        BlogPost savedPost = blogPostRepository.save(blogPost);

        return blogPostMapper.toDto(savedPost);
    }
    @Override
    public Page<ListBlogPostDto> getAllBlogPosts(String title, Pageable pageable)  {
        Page<BlogPost> blogPostings = blogPostRepository.findAllByIsActiveAndTitleContaining(true,
                title, pageable);
        Page<ListBlogPostDto> postingDtoPage = blogPostings.map(listblogPostMapper::toDto);
        return postingDtoPage;
    }
    @Override
    public BlogPostResponseDto updateBlogPost(Integer id, UpdateBlogPostRequestDto updatedPostDTO) throws  OptionalNotFoundException {
        Optional<BlogPost> existingPost = blogPostRepository.findById(id);
        if (existingPost.isEmpty()) {
            throw new OptionalNotFoundException("BlogPost with id " + id + " not found");
        }
        BlogPost post = existingPost.get();
        post.setTitle(updatedPostDTO.getTitle());
        post.setContent(updatedPostDTO.getContent());
        BlogPost updatedPost = blogPostRepository.save(post);
        return blogPostMapper.toDto(updatedPost);
    }
    @Override
    public BlogPostResponseDto deActiveBlogPost(Integer id) throws  OptionalNotFoundException {
        Optional<BlogPost> existingPost = blogPostRepository.findById(id);
        if (existingPost.isEmpty()) {
            throw new OptionalNotFoundException("BlogPost with id " + id + " not found");
        }
        BlogPost post = existingPost.get();
        post.setIsActive(false);
        BlogPost updatedPost = blogPostRepository.save(post);
        return blogPostMapper.toDto(updatedPost);
    }
    @Override
    public BlogPostResponseDto getBlogPostingDetailById(Integer postingId) throws OptionalNotFoundException {
        BlogPost blogPost = blogPostRepository.findById(postingId)
                .orElseThrow(() -> new OptionalNotFoundException("Posting not found with ID: " + postingId));
        return blogPostMapper.toDto(blogPost);
    }
}