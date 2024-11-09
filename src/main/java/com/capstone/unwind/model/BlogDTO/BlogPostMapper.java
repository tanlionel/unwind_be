package com.capstone.unwind.model.BlogDTO;

import com.capstone.unwind.entity.BlogPost;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BlogPostMapper {


    BlogPost toEntity(BlogPostResponseDto blogPostResponseDto);


    BlogPostResponseDto toDto(BlogPost blogPost);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    BlogPost partialUpdate(BlogPostResponseDto blogPostResponseDto, @MappingTarget BlogPost blogPost);


}