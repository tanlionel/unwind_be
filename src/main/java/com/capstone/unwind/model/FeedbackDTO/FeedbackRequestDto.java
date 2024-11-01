package com.capstone.unwind.model.FeedbackDTO;

import com.capstone.unwind.entity.Feedback;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Feedback}
 */
@Value
public class FeedbackRequestDto implements Serializable {
    Float ratingPoint;
    String comment;
}