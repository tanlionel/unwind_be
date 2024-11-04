package com.capstone.unwind.model.FeedbackDTO;

import com.capstone.unwind.entity.Customer;
import com.capstone.unwind.entity.Feedback;
import com.capstone.unwind.entity.Resort;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link Feedback}
 */
@Value
public class FeedbackReportResponseDto implements Serializable {
    Integer id;
    Float ratingPoint;
    String comment;
    String note;
    ResortDto resort;
    CustomerDto customer;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    Timestamp createdDate;
    Boolean isActive;
    Boolean isReport;

    /**
     * DTO for {@link Resort}
     */
    @Value
    public static class ResortDto implements Serializable {
        Integer id;
        String resortName;
    }

    /**
     * DTO for {@link Customer}
     */
    @Value
    public static class CustomerDto implements Serializable {
        Integer id;
        String fullName;
        String avatar;

    }
}