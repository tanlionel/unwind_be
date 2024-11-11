package com.capstone.unwind.model.BlogDTO;

import com.capstone.unwind.entity.BlogPost;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

/**
 * DTO for {@link BlogPost}
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBlogPostRequestDto implements Serializable {
    String title;
    String image;
    Map<String, String> content;
}