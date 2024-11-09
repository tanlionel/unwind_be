package com.capstone.unwind.model.BlogDTO;

import com.capstone.unwind.entity.BlogPost;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Value;

import java.io.Serializable;
import java.util.Map;

/**
 * DTO for {@link BlogPost}
 */
@Value
public class UpdateBlogPostRequestDto implements Serializable {
    String title;
    String image;
    Map<String, String> content;
}