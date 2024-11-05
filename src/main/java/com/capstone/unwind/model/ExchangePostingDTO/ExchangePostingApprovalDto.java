package com.capstone.unwind.model.ExchangePostingDTO;

import com.capstone.unwind.entity.ExchangePosting;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link ExchangePosting}
 */
@Value
public class ExchangePostingApprovalDto implements Serializable {
    String note;
    Integer unitTypeId;
}