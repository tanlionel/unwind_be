package com.capstone.unwind.model.BlogDTO;

import com.capstone.unwind.entity.BlogPost;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for {@link BlogPost}
 */
@Value
public class BlogPostResponseDto implements Serializable {
    Integer id;
    String title;
    String image;
    String content;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Bangkok")
    LocalDateTime createdAt;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Bangkok")
    LocalDateTime updatedAt;
    Boolean isActive;
}