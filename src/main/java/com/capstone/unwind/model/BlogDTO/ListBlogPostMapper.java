package com.capstone.unwind.model.BlogDTO;

import com.capstone.unwind.entity.BlogPost;
import com.capstone.unwind.model.BlogDTO.ListBlogPostDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ListBlogPostMapper {
    BlogPost toEntity(ListBlogPostDto listBlogPostDto);

    ListBlogPostDto toDto(BlogPost blogPost);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    BlogPost partialUpdate(ListBlogPostDto listBlogPostDto, @MappingTarget BlogPost blogPost);
}