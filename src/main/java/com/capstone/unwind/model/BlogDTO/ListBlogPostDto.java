package com.capstone.unwind.model.BlogDTO;

import com.capstone.unwind.entity.BlogPost;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link BlogPost}
 */
@Value
public class ListBlogPostDto implements Serializable {
    Integer id;
    String title;
    String image;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Bangkok")
    LocalDateTime createdAt;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Bangkok")
    LocalDateTime updatedAt;
    Boolean isActive;
}