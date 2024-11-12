package com.capstone.unwind.repository;

import com.capstone.unwind.entity.BlogPost;
import com.capstone.unwind.model.BlogDTO.ListBlogPostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogPostRepository extends JpaRepository<BlogPost, Integer> {
    List<BlogPost> findByIsActiveTrue();



    Page<BlogPost> findAllByIsActiveAndTitleContaining(boolean isActive, String title, Pageable pageable);
}